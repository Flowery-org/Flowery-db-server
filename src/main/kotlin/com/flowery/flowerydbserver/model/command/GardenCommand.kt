package com.flowery.flowerydbserver.model.command

data class CreateGardenCommand(
    val gid: String,
    val key: String?
)

data class UpdateGardenCommand(
    val gid: String,
    val key: String?
)

data class DeleteGardenCommand(
    val gid: String
)
