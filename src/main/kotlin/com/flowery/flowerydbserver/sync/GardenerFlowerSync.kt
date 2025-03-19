package com.flowery.flowerydbserver.sync

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import com.flowery.flowerydbserver.model.document.GardenerFlowerDocument
import com.flowery.flowerydbserver.repository.GardenerFlowerReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GardenerFlowerSync(
    @Autowired private val gardenerFlowerReadRepository: GardenerFlowerReadRepository,
) {
    @RabbitListener(queues = [SyncQueueNameList.GARDENERFLOWER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "sync.gardener_flower.upsert" -> upsertGardenerFlower(message)
            "sync.gardener_flower.delete" -> deleteGardenerFlower(message)
        }
    }

    private fun upsertGardenerFlower(message: Message) {

        val doc = Gson().fromJson(
            String(message.body),
            GardenerFlowerDocument::class.java
        )
        try {
            gardenerFlowerReadRepository.save(doc)
        } catch (e: Exception) {
            // TODO: Handle exception
        }
    }

    private fun deleteGardenerFlower(message: Message) {

        val payload = Gson().fromJson(
            String(message.body),
            Map::class.java
        )

        val gardenerId = payload["uid"] as? String
        val flowerId = payload["fid"] as? String

        try {
            if (gardenerId != null && flowerId != null) {
                gardenerFlowerReadRepository.deleteByGardenerIdAndFlowerId(gardenerId, flowerId)
            }
        } catch (e: Exception) {
            // TODO: Handle exception
        }
    }
}