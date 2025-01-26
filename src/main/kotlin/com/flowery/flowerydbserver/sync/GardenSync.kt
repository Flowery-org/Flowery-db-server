package com.flowery.flowerydbserver.sync

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import com.flowery.flowerydbserver.model.document.GardenDocument
import com.flowery.flowerydbserver.repository.GardenReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GardenSync(
    @Autowired private val gardenReadRepository: GardenReadRepository
) {
    @RabbitListener(queues = [SyncQueueNameList.GARDEN_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "sync.garden.upsert" -> upsertGarden(message)
            "sync.garden.delete" -> deleteGarden(message)
        }
    }

    private fun upsertGarden(message: Message) {
        val doc = Gson().fromJson(String(message.body), GardenDocument::class.java)
        try {
            gardenReadRepository.save(doc)
        } catch (e: Exception) {
            println("Garden upsert failed: ${e.message}")
            // TODO: handle
        }
    }

    private fun deleteGarden(message: Message) {
        val payload = Gson().fromJson(String(message.body), Map::class.java)
        val gardenId = payload["id"] as? String
        if (gardenId != null) {
            try {
                gardenReadRepository.deleteById(gardenId)
            } catch (e: Exception) {
                println("Garden delete failed: ${e.message}")
                // TODO: handle
            }
        }
    }
}