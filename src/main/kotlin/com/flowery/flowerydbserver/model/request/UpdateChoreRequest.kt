package com.flowery.flowerydbserver.model.request

data class UpdateChoreRequest(
    val content: String?,
    val finished: Boolean
)