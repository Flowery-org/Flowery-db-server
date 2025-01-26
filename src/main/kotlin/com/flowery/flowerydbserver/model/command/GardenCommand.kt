package com.flowery.flowerydbserver.model.command

data class CreateGardenCommand(
    val uid: String,
    val key: String?
)

data class UpdateGardenCommand(
    val id: String,
    val key: String?
)

data class DeleteGardenCommand(
    val id: String
)