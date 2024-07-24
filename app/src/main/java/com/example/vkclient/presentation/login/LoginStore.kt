package com.example.vkclient.presentation.login

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.vkclient.domain.entity.AuthState
import com.example.vkclient.domain.usecases.CheckAuthStateUseCase
import com.example.vkclient.domain.usecases.GetAuthStateFlowUseCase
import com.example.vkclient.presentation.login.LoginStore.Intent
import com.example.vkclient.presentation.login.LoginStore.Label
import com.example.vkclient.presentation.login.LoginStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface LoginStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object LoginClick : Intent

        data object SuccessAuth : Intent

        data object LoginContractHasFinished : Intent
    }

    data class State(val state: LoginState) {

        sealed interface LoginState {

            data object Initial : LoginState

            data object Loading : LoginState

            data object Error : LoginState

            data object Authorized : LoginState

            data object NotAuthorized : LoginState
        }
    }

    sealed interface Label {

        data object LoginClick : Label

        data object SuccessAuth : Label
    }
}

class LoginStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) {

    fun create(): LoginStore =
        object : LoginStore, Store<Intent, State, Label> by storeFactory.create(
            name = "LoginStore",
            initialState = State(state = State.LoginState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object StartLoading : Action

        data object Error : Action

        data class LoadingFinished(val authState: AuthState) : Action

        data object ContractHasEnd : Action
    }

    private sealed interface Msg {

        data object StartLoading : Msg

        data object Error : Msg

        data class LoadingFinished(val authState: AuthState) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

            try {
                scope.launch {
                    getAuthStateFlowUseCase().collect {
                        dispatch(Action.LoadingFinished(it))
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
                Intent.LoginClick -> {
                    try {
                        scope.launch {
                            dispatch(Msg.StartLoading)
                            checkAuthStateUseCase()
                        }
                    } catch (e: Exception) {
                        dispatch(Msg.Error)
                    }
                }

                Intent.SuccessAuth -> {
                    publish(Label.LoginClick)
                }

                Intent.LoginContractHasFinished -> {
                    publish(Label.SuccessAuth)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                Action.Error -> {
                    dispatch(Msg.Error)
                }

                is Action.LoadingFinished -> {

                    dispatch(Msg.LoadingFinished(action.authState))
                }

                Action.StartLoading -> {
                    dispatch(Msg.StartLoading)
                }

                Action.ContractHasEnd -> {
                    try {
                        scope.launch {
                            dispatch(Msg.StartLoading)
                            checkAuthStateUseCase()
                        }
                    } catch (e: Exception) {
                        dispatch(Msg.Error)
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.Error -> {
                    this.copy(state = State.LoginState.Error)
                }

                is Msg.LoadingFinished -> {
                    when (msg.authState) {
                        AuthState.Authorized -> {
                            this.copy(state = State.LoginState.Authorized)
                        }

                        else -> {
                            this.copy(state = State.LoginState.NotAuthorized)
                        }
                    }
                }

                Msg.StartLoading -> {
                    this.copy(state = State.LoginState.Loading)
                }
            }
    }
}
