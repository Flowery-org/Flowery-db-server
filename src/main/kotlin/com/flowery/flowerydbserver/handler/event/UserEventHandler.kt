package com.flowery.flowerydbserver.handler.event

import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.model.entity.UserEntity
import com.flowery.flowerydbserver.model.event.UserCreatedEvent
import com.flowery.flowerydbserver.model.event.UserUpdatedEvent
import com.flowery.flowerydbserver.repository.UserReadRepository
import com.flowery.flowerydbserver.repository.UserWriteRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserEventHandler(
    @Autowired private val userWriteRepository: UserWriteRepository,
    @Autowired private val userReadRepository: UserReadRepository
) {
    //* Handles User Created Event
    @EventHandler
    fun on(evt: UserCreatedEvent) {
        //* Save in both readdb and writedb

        val writeModel = UserEntity().apply {
            this.id = evt.uid
            this.name = evt.name
        }

        this.userWriteRepository.save(writeModel)

        //val readModel = UserDocument(evt.uid, evt.name)
        //this.userReadRepository.save(readModel)
    }

    @EventHandler
    fun on(evt: UserUpdatedEvent){
        this.userWriteRepository.findById(evt.uid.toString()).ifPresent {
            it.name = evt.newName
            userWriteRepository.save(it)
        }

        this.userReadRepository.findById(evt.uid.toString()).ifPresent {
            userReadRepository.save(it.copy(name = evt.newName))
        }
    }

}