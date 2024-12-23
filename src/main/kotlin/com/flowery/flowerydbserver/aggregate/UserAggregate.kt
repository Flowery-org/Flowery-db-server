package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.command.CreateUserCommand
import com.flowery.flowerydbserver.command.UpdateUserCommand
import com.flowery.flowerydbserver.model.event.UserCreatedEvent
import com.flowery.flowerydbserver.model.event.UserUpdatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID


//* Aggregate handles commands and maintains state through event
@Aggregate
class UserAggregate {
    @AggregateIdentifier
    private lateinit var uid: UUID
    private lateinit var name: String

    //* Required by Axon
    constructor()

    // Handles create command and emits creation event for state change
    @CommandHandler
    fun handle(command: CreateUserCommand) {
        apply { UserCreatedEvent(command.uid, command.name) }
    }

    // Updates state based on the event
    @EventSourcingHandler
    fun on(evt: UserCreatedEvent) {
        uid = evt.uid
        name = evt.name
    }

    //Handles update command and emits update event for the state change
    @CommandHandler
    fun handle(command: UpdateUserCommand){
        apply { UserUpdatedEvent(command.uid, command.newName) }
    }

    // Updates state based on the event
    @EventSourcingHandler
    fun on(evt: UserUpdatedEvent){
        name = evt.newName
    }
}