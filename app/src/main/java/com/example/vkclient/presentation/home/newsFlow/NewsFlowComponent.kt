package com.example.vkclient.presentation.home.newsFlow

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.vkclient.presentation.home.newsFlow.comments.CommentsComponent
import com.example.vkclient.presentation.login.LoginComponent
import com.example.vkclient.presentation.home.newsFlow.news.NewsFeedComponent

interface NewsFlowComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class NewsFeed(val component: NewsFeedComponent) : Child

        data class Comments(val component: CommentsComponent) : Child
    }
}