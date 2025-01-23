package com.flowery.flowerydbserver.model.request

data class CreateChoreRequest(
    val uid: String,
    val sid: String,
    val fid: String?,
    val content: String?
)