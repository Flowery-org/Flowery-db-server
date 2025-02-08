package com.flowery.flowerydbserver.model.request

data class CreateChoreRequest(
    val gid: String,   // Garden ID
    val gfid: String,  // GardenerFlower ID
    val content: String
)
