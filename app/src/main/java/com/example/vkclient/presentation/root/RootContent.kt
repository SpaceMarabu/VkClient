package com.example.vkclient.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.vkclient.presentation.comments.CommentsContent
import com.example.vkclient.presentation.news.NewsFeedContent
import com.example.vkclient.presentation.root.RootComponent
import com.example.vkclient.ui.theme.VkClientTheme

@Composable
fun RootContent(component: RootComponent) {

    VkClientTheme {
        Children(
            stack = component.stack
        ) {

            when(val instance = it.instance) {
                is RootComponent.Child.Comments -> {
                    CommentsContent(component = instance.component)
                }
                is RootComponent.Child.NewsFeed -> {
                    NewsFeedContent(component = instance.component)
                }
            }
        }
    }

}