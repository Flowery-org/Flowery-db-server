package com.flowery.flowerydbserver.model.request.gardenerflower

data class UpdateGardenerFlowerBlossomRequest (
    val uid: String, // gardener ID
    val fid: String,
    val isBlossom: Boolean,
)