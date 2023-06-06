package com.dawnt.mangareader.screens

import com.dawnt.mangareader.R

sealed class AppScreens(val route: String) {
    object SplashScreen : AppScreens("splash")
}

sealed class NavScreens(val route: String, val icon: Int) {
    object HomeScreen : NavScreens("home", R.drawable.outline_home_24)
    object Library : NavScreens("library", R.drawable.outline_book_shelf_24)

    object UserScreen : NavScreens("user", R.drawable.outline_account_24)
    object ConfigScreen : NavScreens("config", R.drawable.outline_settings_24)
    object Screens {
        val list = listOf(HomeScreen, Library, UserScreen, ConfigScreen)
    }
}

sealed class MangaScreens(val route: String) {
    object MangaDetails : MangaScreens("manga-details")
    object MangaChapter : MangaScreens("manga-chapter")
}