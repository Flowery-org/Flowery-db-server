package com.flowery.flowerydbserver.model.command

data class CreateChoreCommand(
    val gid: String,    // gardenId
    val gfid: String,   // gardenerFlowerId
    val content: String // non-null
)

data class UpdateChoreCommand(
    val choreId: String,
    val content: String,
    val finished: Boolean
)

data class DeleteChoreCommand(
    val choreId: String
)