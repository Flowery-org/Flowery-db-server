package com.flowery.flowerydbserver.sync

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import com.flowery.flowerydbserver.model.document.ChoreDocument
import com.flowery.flowerydbserver.model.document.FlowerDocument
import com.flowery.flowerydbserver.repository.ChoreReadRepository
import com.flowery.flowerydbserver.repository.FlowerReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FlowerSync(
    @Autowired private val flowerReadRepository: FlowerReadRepository
) {
    @RabbitListener(queues = [SyncQueueNameList.FLOWER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "sync.flower.upsert" -> upsertFlower(message)
            "sync.flower.delete" -> deleteFlower(message)
        }
    }

    private fun upsertFlower(message: Message) {

        val doc = Gson().fromJson(
            String(message.body),
            FlowerDocument::class.java
        )
        try {
            flowerReadRepository.save(doc)
        } catch (e: Exception) {
            // TODO: Handle exception
        }
    }

    private fun deleteFlower(message: Message) {

        val payload = Gson().fromJson(
            String(message.body),
            Map::class.java
        )

        val flowerId = payload["id"] as? String
        try {
            if (flowerId != null) {
                flowerReadRepository.deleteById(flowerId)
            }
        } catch (e: Exception) {
            // TODO: Handle exception
        }
    }
}