package com.dawnt.mangareader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dawnt.mangareader.schemas.AppScreens
import com.dawnt.mangareader.schemas.ChapterScheme
import com.dawnt.mangareader.schemas.MangaScreens
import com.dawnt.mangareader.schemas.NavScreens
import com.dawnt.mangareader.screens.Home
import com.dawnt.mangareader.screens.Library
import com.dawnt.mangareader.screens.SplashController
import com.dawnt.mangareader.screens.User
import com.dawnt.mangareader.screens.config.Config
import com.dawnt.mangareader.screens.manga.MangaChapter
import com.dawnt.mangareader.screens.manga.MangaDetail
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DataStore and APIClient
        lifecycleScope.launch {
            DataStoreManager.init(
                context = this@MainActivity
            )
        }

        enableEdgeToEdge()
        setContent {
            val navController: NavHostController = rememberNavController()
            APIClient.build()

            NavHost(
                navController = navController,
                startDestination = AppScreens.SplashScreen.route
            ) {
                // TransitionScreens
                composable(route = AppScreens.SplashScreen.route) { SplashController(navController) }

                // MainScreens
                composable(route = NavScreens.HomeScreen.route) { Home(navController) }
                composable(route = NavScreens.Library.route) { Library(navController) }
                composable(route = NavScreens.UserScreen.route) { User(navController) }
                composable(route = NavScreens.ConfigScreen.route) { Config(navController) }

                // MangaScreens
                composable(route = MangaScreens.MangaDetails.route) { MangaDetail(navController) }
                composable(
                    route = MangaScreens.MangaChapter.route
                            + "?server={server}"
                            + "&nameURL={nameURL}"
                            + "&chapterName={chapterName}"
                            + "&chapterURL={chapterURL}"
                            + "&fromUserView={fromUserView}",
                    arguments = listOf(
                        navArgument("server") { type = NavType.IntType },
                        navArgument("nameURL") { type = NavType.StringType },
                        navArgument("chapterName") { type = NavType.StringType },
                        navArgument("chapterURL") { type = NavType.StringType },
                        navArgument("fromUserView") {
                            type = NavType.BoolType
                            defaultValue = false
                        }
                    )
                ) {
                    it.arguments?.let { args ->
                        MangaChapter(
                            server = args.getInt("server"),
                            nameURL = args.getString("nameURL")!!,
                            chapter = ChapterScheme(
                                name = args.getString("chapterName")!!,
                                url_name = args.getString("chapterURL")!!
                            ),
                            navController = navController,
                            fromUserView = args.getBoolean("fromUserView")
                        )
                    }
                }

            }
        }
    }
}
