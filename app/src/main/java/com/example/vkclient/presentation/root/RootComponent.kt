package com.example.vkclient.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.vkclient.presentation.comments.CommentsComponent
import com.example.vkclient.presentation.news.NewsFeedComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

//        data class Favourite(val component: FavouriteComponent) : Child

        data class NewsFeed(val component: NewsFeedComponent) : Child

//        data class Profile(val component: DetailsComponent) : Child

        data class Comments(val component: CommentsComponent) : Child
    }
}