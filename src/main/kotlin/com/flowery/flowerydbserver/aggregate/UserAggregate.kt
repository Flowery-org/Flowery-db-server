package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateUserCommand
import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.model.entity.UserEntity
import com.flowery.flowerydbserver.repository.UserWriteRepository
import com.google.gson.Gson
import jakarta.persistence.LockModeType
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Component


//* Aggregate handles commands and maintains state through event
@Component
class UserAggregate(
    @Autowired private val userWriteRepository: UserWriteRepository,
    @Autowired private val syncGateway: SyncGateway
) {
    @RabbitListener(queues = [CommandQueueNameList.USER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey

        when(rk) {
            "command.user.create" -> createUser(message)
        }
    }

    private fun createUser(message: Message) {
        val command = Gson().fromJson(String(message.body), CreateUserCommand::class.java)
        val newEntity = UserEntity().apply {
            name = command.name
        }
        var savedEntity: UserEntity? = null;
        //println(newEntity)
        try{
            savedEntity = userWriteRepository.save(newEntity)
        }catch (e: Exception){
            //* TODO: Need to add exception class
        }

        if(savedEntity != null) {
            //Initialized
            val newDocument = UserDocument(
                savedEntity.id,
                savedEntity.name,
            )

            this.syncGateway.send(newDocument, "user", "upsert")
        }
    }
}