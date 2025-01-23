package com.flowery.flowerydbserver.model.request

data class CreateSectorRequest(
    val gid: String,
    val fid: String?,
    val date: String?
)