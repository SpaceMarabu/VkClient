package com.example.vkclient.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.example.vkclient.presentation.home.bottomNavigationSupport.NavigationKey
import com.example.vkclient.presentation.home.newsFlow.NewsFlowComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class HomeFlowComponentImpl @AssistedInject constructor(
    private val newsFlowComponentFactory: NewsFlowComponentImpl.Factory,
    @Assisted componentContext: ComponentContext
) : HomeFlowComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, HomeFlowComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.NewsFlow,
        handleBackButton = true,
        childFactory = ::child
    )

    override fun onHomeClicked() = navigation.bringToFront(Config.NewsFlow)

    override fun onFavouriteClicked() = navigation.bringToFront(Config.Favourite)

    override fun onProfileClicked() = navigation.bringToFront(Config.Profile)

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): HomeFlowComponent.Child {
        return when (config) {

            Config.NewsFlow -> {
                val component = newsFlowComponentFactory.create(
                    componentContext = componentContext
                )
                HomeFlowComponent.Child.NewsFlow(component, NavigationKey.HOME)
            }

            Config.Favourite -> {
                //костыль
                HomeFlowComponent.Child.FavouriteFlow()
            }
            Config.Profile -> {
                //костыль
                HomeFlowComponent.Child.ProfileFlow()
            }
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object NewsFlow : Config

        @Parcelize
        data object Favourite : Config

        @Parcelize
        data object Profile : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted componentContext: ComponentContext
        ): HomeFlowComponentImpl
    }
}