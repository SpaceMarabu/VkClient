package com.example.vkclient.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.presentation.comments.CommentsComponentImpl
import com.example.vkclient.presentation.news.NewsFeedComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class RootComponentImpl @AssistedInject constructor(
    private val commentsComponentFactory: CommentsComponentImpl.Factory,
    private val newsFeedComponentFactory: NewsFeedComponentImpl.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {

            is Config.Comments -> {
                val component = commentsComponentFactory.create(
                    componentContext = componentContext,
                    feedPost = config.feedPost,
                    onBackClicked = { navigation.pop() }
                )
                RootComponent.Child.Comments(component)
            }

            Config.Home -> {
                val component = newsFeedComponentFactory.create(
                    componentContext = componentContext,
                    onCommentClicked = { navigation.push(Config.Comments(it)) }
                )
                RootComponent.Child.NewsFeed(component)
            }
        }
    }


    sealed interface Config : Parcelable {

        @Parcelize
        data class Comments(val feedPost: FeedPost) : Config

        @Parcelize
        data object Home : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted componentContext: ComponentContext
        ): RootComponentImpl
    }
}