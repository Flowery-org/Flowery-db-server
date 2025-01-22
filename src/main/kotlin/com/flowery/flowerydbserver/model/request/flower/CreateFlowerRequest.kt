package com.flowery.flowerydbserver.model.request.flower

data class CreateFlowerRequest(
    val uid: String, // gardener ID
)

data class DeleteFlowerRequest(
    val id: String, // flower ID
)