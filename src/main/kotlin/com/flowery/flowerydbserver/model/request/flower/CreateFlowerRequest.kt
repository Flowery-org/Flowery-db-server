package com.flowery.flowerydbserver.model.request.flower

data class CreateFlowerRequest(
    val uid: String, // gardener ID
)

data class DeleteFlowerRequest(
    val uid: String, // gardener ID
    val fid: String, // flower ID
)