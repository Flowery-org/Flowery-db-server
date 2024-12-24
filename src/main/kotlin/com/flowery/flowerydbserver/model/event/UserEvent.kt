package com.flowery.flowerydbserver.model.event

data class UserCreatedEvent (
    val uid: String,
    val name: String,
)

data class UserUpdatedEvent (
    val uid: String,
    val newName: String,
)