package com.example.vkclient.presentation.home.newsFlow.news

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.domain.usecases.ChangeLikeStatusUseCase
import com.example.vkclient.domain.usecases.DeletePostUseCase
import com.example.vkclient.domain.usecases.GetRecommendationsUseCase
import com.example.vkclient.domain.usecases.LoadNextDataUseCase
import com.example.vkclient.presentation.home.newsFlow.news.NewsFeedStore.Intent
import com.example.vkclient.presentation.home.newsFlow.news.NewsFeedStore.Label
import com.example.vkclient.presentation.home.newsFlow.news.NewsFeedStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface NewsFeedStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class LikeClicked(val feedPost: FeedPost) : Intent

        data class CommentClicked(val feedPost: FeedPost) : Intent

        data object VisibleContentHasScrolled : Intent

        data class DeletePost(val feedPost: FeedPost) : Intent
    }

    data class State(
        val state: NewsFeedState
    ) {

        sealed interface NewsFeedState {

            data object Initial : NewsFeedState

            data object Loading : NewsFeedState

            data object Error : NewsFeedState

            data class Content(
                val feedPosts: List<FeedPost>,
                val nextDataIsLoading: Boolean = false
            ) : NewsFeedState
        }
    }

    sealed interface Label {

        data class OnCommentsClicked(val feedPost: FeedPost) : Label

//        data class OnLikeClicked(val feedPost: FeedPost) : Label
    }
}

class NewsFeedStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) {
    fun create(): NewsFeedStore =
        object : NewsFeedStore, Store<Intent, State, Label> by storeFactory.create(
            name = "NewsFeedStore",
            initialState = State(State.NewsFeedState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {


        data object StartLoading : Action

        data class StartLoadingNextData(val oldPosts: List<FeedPost>) : Action

        data object Error : Action

        data class PostsLoaded(val posts: List<FeedPost>) : Action
    }

    private sealed interface Msg {

        data object StartLoading : Msg

        data class StartLoadingNextData(val posts: List<FeedPost>) : Msg

        data class PostsLoaded(val posts: List<FeedPost>) : Msg

        data object Error : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.StartLoading)
            try {
                scope.launch {
                    getRecommendationsUseCase().collect { feedPosts ->
                        if (feedPosts.isNotEmpty()) {
                            dispatch(Action.PostsLoaded(feedPosts))
                        }
                    }
                }
            } catch (e: Exception) {
                dispatch(Action.Error)
            }

        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {

            when (intent) {
                is Intent.CommentClicked -> {
                    publish(Label.OnCommentsClicked(intent.feedPost))
                }

                is Intent.LikeClicked -> {
                    scope.launch {
                        changeLikeStatusUseCase(intent.feedPost)
                    }
                }

                is Intent.VisibleContentHasScrolled -> {
                    val currentState = getState().state
                    if (currentState is State.NewsFeedState.Content) {
                        val oldPosts = currentState.feedPosts
                        dispatch(Msg.StartLoadingNextData(posts = oldPosts))
                        scope.launch {
                            loadNextDataUseCase()
                        }
                    }
                }

                is Intent.DeletePost -> {
                    scope.launch {
                        deletePostUseCase(intent.feedPost)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {

            when (action) {
                Action.Error -> {
                    dispatch(Msg.Error)
                }

                is Action.PostsLoaded -> {
                    dispatch(Msg.PostsLoaded(action.posts))
                }

                Action.StartLoading -> {
                    dispatch(Msg.StartLoading)
                }

                is Action.StartLoadingNextData -> {
                    dispatch(Msg.StartLoadingNextData(posts = action.oldPosts))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.Error -> {
                    this.copy(state = State.NewsFeedState.Error)
                }

                Msg.StartLoading -> {
                    this.copy(state = State.NewsFeedState.Loading)
                }

                is Msg.StartLoadingNextData -> {
                    val oldPosts = msg.posts
                    this.copy(
                        state = State.NewsFeedState.Content(
                            feedPosts = oldPosts,
                            nextDataIsLoading = true
                        )
                    )
                }

                is Msg.PostsLoaded -> {
                    this.copy(
                        state = State.NewsFeedState.Content(
                            feedPosts = msg.posts,
                            nextDataIsLoading = false
                        )
                    )
                }
            }
    }
}
