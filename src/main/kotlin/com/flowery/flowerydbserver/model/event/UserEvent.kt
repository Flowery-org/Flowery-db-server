package com.flowery.flowerydbserver.model.event

import java.util.UUID

data class UserCreatedEvent (
    val uid: UUID,
    val name: String,
)

data class UserUpdatedEvent (
    val uid: UUID,
    val newName: String,
)