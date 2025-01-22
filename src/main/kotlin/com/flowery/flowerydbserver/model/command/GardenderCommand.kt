package com.flowery.flowerydbserver.model.command

data class CreateGardenerCommand(
    val ident: String,
    val password: String,
    val email: String,
    val name: String,
    val nickname: String,
    val status: String = "NORMAL"
)

data class UpdateGardenerStatusCommand(
    val id: String,
    val status: String,
)

data class UpdateGardenerPasswordCommand(
    val id: String,
    val newPassword: String
)

data class UpdateGardenerNameCommand(
    val id: String,
    val name: String
)

data class UpdateGardenerNicknameCommand(
    val id: String,
    val nickname: String
)

data class DeleteGardenerCommand(
    val id: String
)