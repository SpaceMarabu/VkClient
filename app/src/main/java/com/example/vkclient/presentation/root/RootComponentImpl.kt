package com.example.vkclient.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.example.vkclient.presentation.home.HomeFlowComponentImpl
import com.example.vkclient.presentation.login.LoginComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class RootComponentImpl @AssistedInject constructor(
    private val homeFlowComponentFactory: HomeFlowComponentImpl.Factory,
    private val loginComponentFactory: LoginComponentImpl.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Login,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {

            Config.Login -> {
                val component = loginComponentFactory.create(
                    componentContext = componentContext,
                    onAuthSuccessCB = {
                        navigation.replaceCurrent(Config.HomeFlow)
                    }
                )
                RootComponent.Child.Login(component)
            }

            Config.HomeFlow -> {
                val component = homeFlowComponentFactory.create(
                    componentContext = componentContext
                )
                RootComponent.Child.HomeFlow(component)
            }
        }
    }


    sealed interface Config : Parcelable {

        @Parcelize
        data object HomeFlow : Config

        @Parcelize
        data object Login : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted componentContext: ComponentContext
        ): RootComponentImpl
    }
}