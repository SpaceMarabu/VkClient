package com.example.vkclient.presentation.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.vkclient.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class LoginComponentImpl @AssistedInject constructor(
    storeFactory: LoginStoreFactory,
    @Assisted private val componentContext: ComponentContext,
    @Assisted("onAuthSuccessCB") private val onAuthSuccessCB: () -> Unit
) : LoginComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    LoginStore.Label.LoginClick -> {
                        store.accept(LoginStore.Intent.LoginClick)
                    }

                    LoginStore.Label.SuccessAuth -> {
                        store.accept(LoginStore.Intent.SuccessAuth)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    override fun onLoginClick() {
        store.accept(LoginStore.Intent.LoginClick)
    }

    override fun onAuthSuccess() = onAuthSuccessCB()
    override fun onLoginContractHasFinished() =
        store.accept(LoginStore.Intent.LoginContractHasFinished)

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted componentContext: ComponentContext,
            @Assisted("onAuthSuccessCB") onAuthSuccessCB: () -> Unit
        ): LoginComponentImpl
    }
}