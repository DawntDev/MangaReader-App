package com.dawnt.mangareader.schemas

import androidx.navigation.NavType
import com.dawnt.mangareader.R

sealed class AppScreens(val route: String) {
    data object SplashScreen : AppScreens("splash")
}

sealed class NavScreens(val route: String, val icon: Int) {
    data object HomeScreen : NavScreens("home", R.drawable.outline_home_24)
    data object Library : NavScreens("library", R.drawable.outline_book_shelf_24)

    data object UserScreen : NavScreens("user", R.drawable.outline_account_24)
    data object ConfigScreen : NavScreens("config", R.drawable.outline_settings_24)
    object Screens {
        val list = listOf(HomeScreen, Library, UserScreen, ConfigScreen)
    }
}

sealed class MangaScreens(val route: String) {
    data object MangaDetails : MangaScreens(route = "manga-details")
    data object MangaChapter : MangaScreens(route = "manga-chapter")
}