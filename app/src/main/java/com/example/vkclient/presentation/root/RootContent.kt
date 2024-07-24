package com.example.vkclient.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.vkclient.presentation.home.HomeFlowContent
import com.example.vkclient.presentation.login.LoginContent
import com.example.vkclient.ui.theme.VkClientTheme

@Composable
fun RootContent(component: RootComponent) {

    VkClientTheme {
        Children(
            stack = component.stack
        ) {

            when(val instance = it.instance) {

                is RootComponent.Child.Login -> {
                    LoginContent(component = instance.component)
                }

                is RootComponent.Child.HomeFlow -> {
                    HomeFlowContent(component = instance.component)
                }
            }
        }
    }

}