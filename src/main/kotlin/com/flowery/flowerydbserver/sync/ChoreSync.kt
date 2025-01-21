package com.flowery.flowerydbserver.sync

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import com.flowery.flowerydbserver.model.document.ChoreDocument
import com.flowery.flowerydbserver.repository.ChoreReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ChoreSync(
    @Autowired private val choreReadRepository: ChoreReadRepository
) {
    @RabbitListener(queues = [SyncQueueNameList.CHORE_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "sync.chore.upsert" -> upsertChore(message)
            "sync.chore.delete" -> deleteChore(message)
        }
    }

    private fun upsertChore(message: Message) {
        // Document JSON 역직렬화
        val doc = Gson().fromJson(String(message.body), ChoreDocument::class.java)
        try {
            choreReadRepository.save(doc)
        } catch (e: Exception) {
            // TODO: Handle exception
            println("Chore upsert failed: ${e.message}")
        }
    }

    private fun deleteChore(message: Message) {
        // { "id": "<chore-id>" } 형태
        val payload = Gson().fromJson(String(message.body), Map::class.java)
        val choreId = payload["id"] as? String
        try {
            if (choreId != null) {
                choreReadRepository.deleteById(choreId)
            }
        } catch (e: Exception) {
            // TODO: Handle exception
            println("Chore delete failed: ${e.message}")
        }
    }
}