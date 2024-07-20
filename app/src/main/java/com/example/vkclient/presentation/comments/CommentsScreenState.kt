package com.example.vkclient.presentation.comments

import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.domain.entity.PostComment

sealed class CommentsScreenState {

    object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}
