package com.flowery.flowerydbserver.model.request

data class CreateSectorRequest(
    val gid: String,   // Garden ID
    val gfid: String,  // GardenerFlower ID
    val date: String?  // optional, e.g. "2025-02-01"
)
