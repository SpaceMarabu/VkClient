package com.example.vkclient.presentation.home.newsFlow.comments

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.extensions.componentScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentsComponentImpl @AssistedInject constructor(
    storeFactory: CommentsStoreFactory,
    @Assisted private val componentContext: ComponentContext,
    @Assisted private val feedPost: FeedPost,
    @Assisted private val onBackClicked: () -> Unit
) : CommentsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(feedPost) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    CommentsStore.Label.ClickBack -> {
                        onBackClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    override fun onBackPressed() {
        store.accept(CommentsStore.Intent.ClickBack)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted componentContext: ComponentContext,
            @Assisted onBackClicked: () -> Unit,
            @Assisted feedPost: FeedPost
        ): CommentsComponentImpl
    }

}