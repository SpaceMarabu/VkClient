package com.example.vkclient.presentation.login

import kotlinx.coroutines.flow.StateFlow

interface LoginComponent {

    val model: StateFlow<LoginStore.State>

    fun onLoginClick()

    fun onAuthSuccess()

    fun onLoginContractHasFinished()
}