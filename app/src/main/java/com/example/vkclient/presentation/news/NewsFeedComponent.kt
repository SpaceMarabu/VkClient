package com.example.vkclient.presentation.news

import com.example.vkclient.domain.entity.FeedPost
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedComponent {

     val model: StateFlow<NewsFeedStore.State>

     fun deletePost(feedPost: FeedPost)

     fun onLikeClicked(feedPost: FeedPost)

     fun onCommentsClicked(feedPost: FeedPost)

     fun onVisibleContentHasScrolled()
}