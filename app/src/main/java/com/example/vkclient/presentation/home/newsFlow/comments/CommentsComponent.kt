package com.example.vkclient.presentation.home.newsFlow.comments

import kotlinx.coroutines.flow.StateFlow

interface CommentsComponent {

     val model: StateFlow<CommentsStore.State>

     fun onBackPressed()
}