package com.example.vkclient.presentation.home.newsFlow

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.vkclient.presentation.home.newsFlow.comments.CommentsContent
import com.example.vkclient.presentation.login.LoginContent
import com.example.vkclient.presentation.home.newsFlow.news.NewsFeedContent
import com.example.vkclient.presentation.root.RootComponent
import com.example.vkclient.ui.theme.VkClientTheme

@Composable
fun NewsFlowContent(
    component: NewsFlowComponent,
    paddingValues: PaddingValues
) {

        Children(
            stack = component.stack
        ) {
            Log.d("TEST_DEC", (component.stack.value.active.configuration).toString())
            when(val instance = it.instance) {
                is NewsFlowComponent.Child.Comments -> {
                    CommentsContent(
                        component = instance.component,
                        paddingValues = paddingValues
                    )
                }
                is NewsFlowComponent.Child.NewsFeed -> {
                    NewsFeedContent(
                        component = instance.component,
                        paddingValues = paddingValues
                    )
                }
            }
        }

}