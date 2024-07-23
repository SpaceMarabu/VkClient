package com.example.vkclient.presentation.comments

import kotlinx.coroutines.flow.StateFlow

interface CommentsComponent {

     val model: StateFlow<CommentsStore.State>

     fun onBackPressed()
}