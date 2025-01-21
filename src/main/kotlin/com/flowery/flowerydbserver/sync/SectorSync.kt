package com.flowery.flowerydbserver.sync

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import com.flowery.flowerydbserver.model.document.SectorDocument
import com.flowery.flowerydbserver.repository.SectorReadRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SectorSync(
    @Autowired private val sectorReadRepository: SectorReadRepository
) {
    @RabbitListener(queues = [SyncQueueNameList.SECTOR_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "sync.sector.upsert" -> upsertSector(message)
            "sync.sector.delete" -> deleteSector(message)
        }
    }

    private fun upsertSector(message: Message) {
        val doc = Gson().fromJson(String(message.body), SectorDocument::class.java)
        try {
            sectorReadRepository.save(doc)
        } catch (e: Exception) {
            println("Sector upsert failed: ${e.message}")
            // TODO: handle
        }
    }

    private fun deleteSector(message: Message) {
        val payload = Gson().fromJson(String(message.body), Map::class.java)
        val sectorId = payload["id"] as? String
        if (sectorId != null) {
            try {
                sectorReadRepository.deleteById(sectorId)
            } catch (e: Exception) {
                println("Sector delete failed: ${e.message}")
                // TODO: handle
            }
        }
    }
}