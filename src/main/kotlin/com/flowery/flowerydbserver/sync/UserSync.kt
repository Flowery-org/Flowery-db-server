package com.flowery.flowerydbserver.sync

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.repository.UserReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserSync(
    @Autowired private val userReadRepository: UserReadRepository
) {
    @RabbitListener(queues = [SyncQueueNameList.USER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey

        when(rk) {
            "sync.user.upsert" -> upsertUser(message)
        }
    }

    private fun upsertUser(message: Message) {
        val upsertDocument = Gson().fromJson(String(message.body), UserDocument::class.java)

        try{
            this.userReadRepository.save(upsertDocument)
        }catch (e: Exception){
            //* TODO: Need to add exception class
            println(e.message)
        }
    }
}