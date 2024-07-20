package com.example.vkclient.di

import com.example.vkclient.domain.entity.FeedPost
import com.example.vkclient.presentation.ViewModelFactory
import com.example.vkclient.presentation.comments.CommentsViewModel_Factory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        CommentsViewModelModule::class
    ]
)
interface CommentScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance feedPost: FeedPost
        ): CommentScreenComponent
    }
}