package com.flowery.flowerydbserver.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class CreateUserCommand(
    @TargetAggregateIdentifier
    val uid: UUID,
    val name: String,
)

data class UpdateUserCommand(
    @TargetAggregateIdentifier
    val uid: UUID,
    val newName: String,
)