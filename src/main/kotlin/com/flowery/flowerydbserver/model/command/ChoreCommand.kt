package com.flowery.flowerydbserver.model.command

data class CreateChoreCommand(
    val uid: String,
    val gfid: String, // 이 부분이 gfid로 변경
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