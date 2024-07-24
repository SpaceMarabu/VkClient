package com.example.vkclient.navigation

import com.example.vkclient.presentation.home.bottomNavigationSupport.NavigationKey

sealed class Screen(
    val navigationKey: NavigationKey
) {
    data object Favourite : Screen(NavigationKey.FAVOURITE)
    data object Profile : Screen(NavigationKey.PROFILE)
    data object Home : Screen(NavigationKey.HOME)
}
