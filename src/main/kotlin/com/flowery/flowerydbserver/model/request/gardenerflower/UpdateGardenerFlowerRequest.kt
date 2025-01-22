package com.flowery.flowerydbserver.model.request.gardenerflower
data class UpdateGardenerBlossomRequest (
    val uid: String, // gardener ID
    val fid: String,
    val isBlossom: Boolean,
)