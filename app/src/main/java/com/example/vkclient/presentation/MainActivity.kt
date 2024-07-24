package com.example.vkclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.arkivanov.decompose.defaultComponentContext
import com.example.vkclient.presentation.root.RootComponentImpl
import com.example.vkclient.presentation.root.RootContent
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
            RootContent(component = rootComponent)
        }
    }
}
