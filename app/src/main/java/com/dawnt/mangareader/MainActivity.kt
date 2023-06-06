package com.dawnt.mangareader

import android.graphics.Bitmap
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dawnt.mangareader.screens.AppScreens
import com.dawnt.mangareader.screens.Config
import com.dawnt.mangareader.screens.Home
import com.dawnt.mangareader.screens.Library
import com.dawnt.mangareader.screens.MangaChapter
import com.dawnt.mangareader.screens.MangaDetail
import com.dawnt.mangareader.screens.MangaScreens
import com.dawnt.mangareader.screens.NavScreens
import com.dawnt.mangareader.screens.SplashController
import com.dawnt.mangareader.screens.User
import com.dawnt.mangareader.utils.DataStorage
import com.dawnt.mangareader.utils.MangaReaderConnect
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import android.graphics.Color as AndroidColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { DataStorage.init(this@MainActivity) }
        hideSystemUI()
        setContent {
            Assets.defaultImageBitmap = getDrawable(R.drawable.default_image)?.toBitmap()!!
            Assets.genres = Gson()
                .fromJson<Map<String, String>>(
                    assets
                        .open("genres.json")
                        .bufferedReader()
                        .use { it.readText() },
                    object : TypeToken<Map<String, String>>() {}.type
                ).mapValues { entry -> Color(AndroidColor.parseColor(entry.value)) }

            val navController: NavHostController = rememberNavController()
            val mangaReaderConn = MangaReaderConnect("http://10.0.2.2:8000")
            NavHost(
                navController = navController,
                startDestination = AppScreens.SplashScreen.route
            ) {
                // TransitionScreens
                composable(route = AppScreens.SplashScreen.route) {
                    SplashController(
                        navController,
                        mangaReaderConn
                    )
                }

                // MainScreens
                composable(route = NavScreens.HomeScreen.route) {
                    Home(
                        navController,
                        mangaReaderConn
                    )
                }
                composable(route = NavScreens.Library.route) {
                    Library(
                        navController,
                        mangaReaderConn,
                        Assets.genres
                    )
                }
                composable(route = NavScreens.UserScreen.route) {
                    User(
                        navController,
                        mangaReaderConn
                    )
                }
                composable(route = NavScreens.ConfigScreen.route) { Config(navController) }

                // MangaScreens
                composable(route = MangaScreens.MangaDetails.route) {
                    MangaDetail(
                        navController,
                        mangaReaderConn
                    )
                }
                composable(
                    route = MangaScreens.MangaChapter.route + "?nameURL={nameURL}&chapter={chapter}&fromUserView={fromUserView}",
                    arguments = listOf(
                        navArgument("nameURL") { type = NavType.StringType },
                        navArgument("chapter") { type = NavType.StringType },
                        navArgument("fromUserView") {
                            type = NavType.BoolType
                            defaultValue = false
                        }
                    )
                ) {
                    it.arguments?.let { args ->
                        MangaChapter(
                            args.getString("nameURL")!!,
                            args.getString("chapter")!!,
                            navController,
                            mangaReaderConn,
                            args.getBoolean("fromUserView")
                        )
                    }
                }

            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(window.context, R.color.raisin_black)
    }

//    private fun showSystemUI() {
//        WindowCompat.setDecorFitsSystemWindows(window, true)
//        WindowInsetsControllerCompat(
//            window,
//            window.decorView
//        ).show(WindowInsetsCompat.Type.systemBars())
//    }

    object Assets {
        lateinit var defaultImageBitmap: Bitmap
        lateinit var genres: Map<String, Color>
    }
}
