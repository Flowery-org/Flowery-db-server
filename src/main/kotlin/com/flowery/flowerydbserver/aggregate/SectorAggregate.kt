package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateSectorCommand
import com.flowery.flowerydbserver.model.command.DeleteSectorCommand
import com.flowery.flowerydbserver.model.command.UpdateSectorCommand
import com.flowery.flowerydbserver.model.document.SectorDocument
import com.flowery.flowerydbserver.model.entity.SectorEntity
import com.flowery.flowerydbserver.repository.SectorWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SectorAggregate(
    @Autowired private val sectorWriteRepository: SectorWriteRepository,
    @Autowired private val syncGateway: SyncGateway
) {
    @RabbitListener(queues = [CommandQueueNameList.SECTOR_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "command.sector.create" -> createSector(message)
            "command.sector.update" -> updateSector(message)
            "command.sector.delete" -> deleteSector(message)
        }
    }

    private fun createSector(message: Message) {
        val command = Gson().fromJson(String(message.body), CreateSectorCommand::class.java)
        val dateParsed = command.date?.let { LocalDate.parse(it) }

        val entity = SectorEntity(
            gid = command.gid,
            fid = command.fid,
            date = dateParsed
        )
        val saved = sectorWriteRepository.save(entity)

        val doc = SectorDocument(
            id = saved.id,
            gid = saved.gid,
            fid = saved.fid,
            date = saved.date
        )
        syncGateway.send(doc, "sector", "upsert")
    }

    private fun updateSector(message: Message) {
        val command = Gson().fromJson(String(message.body), UpdateSectorCommand::class.java)
        val entityOpt = sectorWriteRepository.findById(command.id)
        if (entityOpt.isPresent) {
            val sector = entityOpt.get()
            sector.fid = command.fid
            sector.date = command.date?.let { LocalDate.parse(it) }

            val saved = sectorWriteRepository.save(sector)
            val doc = SectorDocument(
                id = saved.id,
                gid = saved.gid,
                fid = saved.fid,
                date = saved.date
            )
            syncGateway.send(doc, "sector", "upsert")
        } else {
            // TODO: 예외 처리
        }
    }

    private fun deleteSector(message: Message) {
        val command = Gson().fromJson(String(message.body), DeleteSectorCommand::class.java)
        val entityOpt = sectorWriteRepository.findById(command.id)
        if (entityOpt.isPresent) {
            sectorWriteRepository.deleteById(command.id)
            syncGateway.send(mapOf("id" to command.id), "sector", "delete")
        } else {
            // TODO: 예외 처리
        }
    }
}