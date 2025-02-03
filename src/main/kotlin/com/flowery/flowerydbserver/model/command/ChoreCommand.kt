package com.flowery.flowerydbserver.model.command

// Create
data class CreateChoreCommand(
    val gid: String,   // Garden ID
    val gfid: String,  // GardenerFlower ID
    val content: String
)

// Update
data class UpdateChoreCommand(
    val choreId: String,
    val content: String,
    val finished: Boolean
)

// Delete
data class DeleteChoreCommand(
    val choreId: String
)
