package com.example.vkclient.data.model

import com.google.gson.annotations.SerializedName
import com.example.vkclient.data.model.AttachmentDto
import com.example.vkclient.data.model.CommentsDto
import com.example.vkclient.data.model.LikesDto
import com.example.vkclient.data.model.RepostsDto
import com.example.vkclient.data.model.ViewsDto

data class PostDto(
    @SerializedName("id") val id: Long,
    @SerializedName("source_id") val communityId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("comments") val comments: CommentsDto,
    @SerializedName("views") val views: ViewsDto,
    @SerializedName("reposts") val reposts: RepostsDto,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?
)
