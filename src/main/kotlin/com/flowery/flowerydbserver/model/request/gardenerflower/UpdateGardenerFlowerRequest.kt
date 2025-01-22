package com.flowery.flowerydbserver.model.request.gardenerflower

data class CreateGardenerFlowerRequest (
    val uid: String, // gardener ID
    val fid: String
)
data class UpdateGardenerFlowerBlossomRequest (
    val uid: String, // gardener ID
    val fid: String,
    val isBlossom: Boolean,
)