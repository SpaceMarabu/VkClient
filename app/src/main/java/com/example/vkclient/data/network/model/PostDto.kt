package com.example.vkclient.data.network.model

import com.google.gson.annotations.SerializedName
import com.example.vkclient.data.network.model.AttachmentDto
import com.example.vkclient.data.network.model.CommentsDto
import com.example.vkclient.data.network.model.LikesDto
import com.example.vkclient.data.network.model.RepostsDto
import com.example.vkclient.data.network.model.ViewsDto

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
