package com.flowery.flowerydbserver.model.command

import com.flowery.flowerydbserver.constant.GardenerStatus

data class CreateGardenerCommand(
    val ident: String,
    val password: String,
    val email: String,
    val name: String,
    val nickname: String,
    val status: GardenerStatus,
)

open class UpdateGardenerCommand(open val id: String)

data class UpdateGardenerStatusCommand(
    override val id: String,
    val status: String,
) : UpdateGardenerCommand(id)

data class UpdateGardenerPasswordCommand(
    override val id: String,
    val newPassword: String
) : UpdateGardenerCommand(id)

data class UpdateGardenerNameCommand(
    override val id: String,
    val name: String
) : UpdateGardenerCommand(id)

data class UpdateGardenerNicknameCommand(
    override val id: String,
    val nickname: String
) : UpdateGardenerCommand(id)

data class DeleteGardenerCommand(
    val id: String
)