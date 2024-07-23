package com.example.vkclient.presentation.comments

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.domain.entity.PostComment
import com.example.vkclient.domain.repository.NewsFeedRepository
import com.example.vkclient.domain.usecases.GetCommentsUseCase
import com.example.vkclient.presentation.comments.CommentsStore.Intent
import com.example.vkclient.presentation.comments.CommentsStore.Label
import com.example.vkclient.presentation.comments.CommentsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CommentsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent
    }

    data class State(
        val commentsState: CommentsState
    ) {

        sealed interface CommentsState {

            data object Initial : CommentsState

            data object Loading : CommentsState

            data object Error : CommentsState

            data class Content(
                val comments: List<PostComment>
            ) : CommentsState
        }

    }

    sealed interface Label {

        data object ClickBack : Label
    }
}

class CommentsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getCommentsUseCase: GetCommentsUseCase
) {

    fun create(feedPost: FeedPost): CommentsStore =
        object : CommentsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CommentsStore",
            initialState = State(State.CommentsState.Initial),
            bootstrapper = BootstrapperImpl(feedPost),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object StartLoading : Action

        data object Error : Action

        data class CommentsLoaded(val comments: List<PostComment>) : Action
    }

    private sealed interface Msg {

        data class CommentsLoaded(val comments: List<PostComment>) : Msg

        data object StartLoading : Msg

        data object LoadingError : Msg
    }

    private inner class BootstrapperImpl(private val feedPost: FeedPost) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.StartLoading)
            try {
                scope.launch {
                    getCommentsUseCase(feedPost).collect {
                        dispatch(Action.CommentsLoaded(it))
                    }
                }
            } catch (e: Exception) {
                dispatch(Action.Error)
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.CommentsLoaded -> {
                    val comments = action.comments
                    dispatch(Msg.CommentsLoaded(comments))
                }

                Action.StartLoading -> {
                    dispatch(Msg.StartLoading)
                }

                Action.Error -> {
                    dispatch(Msg.LoadingError)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.CommentsLoaded -> {
                    this.copy(commentsState = State.CommentsState.Content(msg.comments))
                }

                Msg.StartLoading -> {
                    this.copy(commentsState = State.CommentsState.Loading)
                }

                Msg.LoadingError -> {
                    this.copy(commentsState = State.CommentsState.Error)
                }
            }
    }
}
