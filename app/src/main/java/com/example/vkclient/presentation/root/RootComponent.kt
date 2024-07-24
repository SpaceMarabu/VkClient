package com.example.vkclient.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.vkclient.presentation.home.HomeFlowComponent
import com.example.vkclient.presentation.login.LoginComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Login(
            val component: LoginComponent
        ) : Child

        data class HomeFlow(
            val component: HomeFlowComponent
        ) : Child
    }
}