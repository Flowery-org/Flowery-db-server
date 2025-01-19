package com.flowery.flowerydbserver.model.command

data class CreateSectorCommand(
    val gid: String,
    val fid: String?,
    val date: String?
)

data class UpdateSectorCommand(
    val id: String,
    val fid: String?,
    val date: String?
)

data class DeleteSectorCommand(
    val id: String
)