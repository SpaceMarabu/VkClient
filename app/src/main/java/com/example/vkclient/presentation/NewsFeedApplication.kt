package com.example.vkclient.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.vkclient.di.ApplicationComponent
import com.example.vkclient.di.DaggerApplicationComponent
import com.example.vkclient.domain.entity.FeedPost

class NewsFeedApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as NewsFeedApplication).component
}
