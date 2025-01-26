package com.flowery.flowerydbserver.model.command
data class CreateGardenerFlowerCommand(
    val uid: String,
    val fid: String,
    val isBlossom: Boolean
)

data class UpdateGardenerFlowerBlossomCommand(
    val uid: String, // gardener ID
    val fid: String,
    val isBlossom: Boolean
)

data class DeleteGardenerFlowerCommand(
    val id: String
)