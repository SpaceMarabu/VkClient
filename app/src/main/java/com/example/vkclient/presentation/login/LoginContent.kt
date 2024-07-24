package com.example.vkclient.presentation.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vkclient.R
import com.example.vkclient.ui.theme.DarkBlue
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

@Composable
fun LoginContent(component: LoginComponent) {

    val state by component.model.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = VK.getVKAuthActivityResultContract()
    ) {
        component.onLoginContractHasFinished()
    }

    when (state.state) {
        LoginStore.State.LoginState.Authorized -> {

            component.onAuthSuccess()
        }

        LoginStore.State.LoginState.Loading -> {

            Loading()
        }

        LoginStore.State.LoginState.NotAuthorized -> {

            Content(onLoginClick = {
                launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                component.onLoginClick()
            })
        }

        else -> {}
    }

}

//<editor-fold desc="Content">
@Composable
private fun Content(
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.vk_logo),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(100.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    contentColor = Color.White
                ),
                onClick = { onLoginClick() }
            ) {
                Text(text = stringResource(R.string.button_login))
            }
        }
    }
}
//</editor-fold>

//<editor-fold desc="Loading">
@Composable
private fun Loading() {
    Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = DarkBlue)
    }
}
//</editor-fold>