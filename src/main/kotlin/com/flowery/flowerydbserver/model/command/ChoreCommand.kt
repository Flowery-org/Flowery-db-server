package com.flowery.flowerydbserver.model.command

data class CreateChoreCommand(
    val uid: String,
    val sid: String,
    val fid: String?,
    val content: String?
)

data class UpdateChoreCommand(
    val id: String,
    val content: String?,
    val finished: Boolean
)

data class DeleteChoreCommand(
    val id: String
)