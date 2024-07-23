package com.example.vkclient.presentation.news

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class NewsFeedComponentImpl @AssistedInject constructor(
    storeFactory: NewsFeedStoreFactory,
    @Assisted private val componentContext: ComponentContext,
    @Assisted("onCommentClicked") private val onCommentClicked: (FeedPost) -> Unit
) : NewsFeedComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    is NewsFeedStore.Label.OnCommentsClicked -> {
                        onCommentClicked(it.feedPost)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    override fun deletePost(feedPost: FeedPost) =
        store.accept(NewsFeedStore.Intent.DeletePost(feedPost))

    override fun onLikeClicked(feedPost: FeedPost) =
        store.accept(NewsFeedStore.Intent.LikeClicked(feedPost))

    override fun onCommentsClicked(feedPost: FeedPost) =
        store.accept(NewsFeedStore.Intent.CommentClicked(feedPost))

    override fun onVisibleContentHasScrolled() =
        store.accept(NewsFeedStore.Intent.VisibleContentHasScrolled)

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted componentContext: ComponentContext,
            @Assisted("onCommentClicked") onCommentClicked: (FeedPost) -> Unit,
//            @Assisted("onLikeClicked") onLikeClicked: (FeedPost) -> Unit
        ): NewsFeedComponentImpl
    }
}