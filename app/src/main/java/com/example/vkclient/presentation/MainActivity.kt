package com.example.vkclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arkivanov.decompose.defaultComponentContext
import com.example.vkclient.domain.entity.AuthState
import com.example.vkclient.presentation.main.LoginScreen
import com.example.vkclient.presentation.main.MainViewModel
import com.example.vkclient.presentation.root.RootComponentImpl
import com.example.vkclient.presentation.root.RootContent
import com.example.vkclient.ui.theme.VkClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.Gray.copy(alpha = 0.5f).toArgb()

        (applicationContext as VkClientApplication).applicationComponent.inject(this)

        val rootComponent = rootComponentFactory.create(defaultComponentContext())

        setContent {
            val component = getApplicationComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(AuthState.Initial)

            val launcher = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract()
            ) {
                viewModel.performAuthResult()
            }

            when (authState.value) {
                is AuthState.Authorized -> {
                    RootContent(component = rootComponent)
                }

                is AuthState.NotAuthorized -> {
                    LoginScreen {
                        launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                    }
                }

                else -> {}
            }
        }
    }
}
