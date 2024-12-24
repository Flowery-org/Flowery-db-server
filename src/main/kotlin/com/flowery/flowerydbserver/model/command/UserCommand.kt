package com.flowery.flowerydbserver.model.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateUserCommand(
    @TargetAggregateIdentifier
    val uid: String,
    val name: String,
)

data class UpdateUserCommand(
    @TargetAggregateIdentifier
    val uid: String,
    val newName: String,
)