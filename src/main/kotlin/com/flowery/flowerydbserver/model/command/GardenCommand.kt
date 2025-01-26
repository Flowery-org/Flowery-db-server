package com.flowery.flowerydbserver.model.command

data class CreateGardenCommand(
    val gardenerId: String,
    val key: String?
)

data class UpdateGardenCommand(
    val gardenId: String,
    val key: String?
)

data class DeleteGardenCommand(
    val gardenId: String
)