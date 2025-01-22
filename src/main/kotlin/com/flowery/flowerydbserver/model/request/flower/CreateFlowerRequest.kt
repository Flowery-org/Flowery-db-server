package com.flowery.flowerydbserver.model.request.flower

data class CreateFlowerRequest(
    val uid: String, // gardener ID
    val kid: String,
    val color: FlowerEntity.FlowerColor
)