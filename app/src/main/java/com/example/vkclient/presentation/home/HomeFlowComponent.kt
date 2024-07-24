package com.example.vkclient.presentation.home

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.vkclient.presentation.home.bottomNavigationSupport.NavigationKey
import com.example.vkclient.presentation.home.newsFlow.NewsFlowComponent

interface HomeFlowComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onHomeClicked()

    fun onFavouriteClicked()

    fun onProfileClicked()

    sealed interface Child {

        data class NewsFlow(
            val component: NewsFlowComponent,
            val navKey: NavigationKey = NavigationKey.HOME
        ) : Child

        data class FavouriteFlow(
            val navKey: NavigationKey = NavigationKey.FAVOURITE
        ) : Child

        data class ProfileFlow(
            val navKey: NavigationKey = NavigationKey.PROFILE
        ) : Child
    }
}