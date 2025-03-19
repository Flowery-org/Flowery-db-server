package com.flowery.flowerydbserver.sync

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import com.flowery.flowerydbserver.model.document.ChoreDocument
import com.flowery.flowerydbserver.model.document.GardenerDocument
import com.flowery.flowerydbserver.repository.ChoreReadRepository
import com.flowery.flowerydbserver.repository.GardenerReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GardenerSync(
    @Autowired private val gardenerReadRepository: GardenerReadRepository
) {
    @RabbitListener(queues = [SyncQueueNameList.GARDENER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "sync.gardener.upsert" -> upsertGardener(message)
            "sync.gardener.delete" -> deleteGardener(message)
        }
    }

    private fun upsertGardener(message: Message) {

        val doc = Gson().fromJson(
            String(message.body),
            GardenerDocument::class.java
        )

        //
        try {
            println("upsertGardner")
            gardenerReadRepository.save(doc)
        } catch (e: Exception) {
            // TODO: Handle exception
        }
    }

    private fun deleteGardener(message: Message) {

        val payload = Gson().fromJson(
            String(message.body),
            Map::class.java
        )

        val gardenerId = payload["id"] as? String
        try {
            if (gardenerId != null) {
                gardenerReadRepository.deleteById(gardenerId)
            }
        } catch (e: Exception) {
            // TODO: Handle exception
        }
    }
}