package com.flowery.flowerydbserver.model.command


data class CreateUserCommand(
    val name: String,
)

data class UpdateUserCommand(
    val uid: String,
    val newName: String,
)