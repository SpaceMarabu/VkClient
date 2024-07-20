package com.example.vkclient.domain.usecases

import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.domain.entity.PostComment
import com.example.vkclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {

    operator fun invoke(feedPost: FeedPost): StateFlow<List<PostComment>> {
        return repository.getComments(feedPost)
    }
}
