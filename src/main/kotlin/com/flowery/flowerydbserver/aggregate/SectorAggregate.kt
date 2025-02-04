package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateSectorCommand
import com.flowery.flowerydbserver.model.command.UpdateSectorCommand
import com.flowery.flowerydbserver.model.command.DeleteSectorCommand
import com.flowery.flowerydbserver.model.document.SectorDocument
import com.flowery.flowerydbserver.model.entity.SectorEntity
import com.flowery.flowerydbserver.model.entity.GardenEntity
import com.flowery.flowerydbserver.model.entity.GardenerFlowerEntity
import com.flowery.flowerydbserver.repository.SectorWriteRepository
import com.flowery.flowerydbserver.repository.GardenWriteRepository
import com.flowery.flowerydbserver.repository.GardenerFlowerWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SectorAggregate(
    private val sectorWriteRepository: SectorWriteRepository,
    private val gardenWriteRepository: GardenWriteRepository,
    private val gardenerFlowerWriteRepository: GardenerFlowerWriteRepository,
    private val syncGateway: SyncGateway
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

        val gardenOpt = gardenWriteRepository.findById(command.gid)
        val gfOpt = gardenerFlowerWriteRepository.findById(command.gfid)
        if (!gardenOpt.isPresent || !gfOpt.isPresent) {
            // TODO: handle error (FK not found)
            return
        }
        val dateParsed = command.date?.let { LocalDate.parse(it) } ?: LocalDate.now()

        val newSector = SectorEntity(
            garden = gardenOpt.get(),
            gardenerFlower = gfOpt.get(),
            date = dateParsed
        )
        val saved = sectorWriteRepository.save(newSector)

        // MongoDB Document 변환
        val doc = SectorDocument(
            id = saved.id,
            gid = saved.garden.id,
            gfid = saved.gardenerFlower.id,
            date = saved.date
        )
        syncGateway.send(doc, "sector", "upsert")
    }

    private fun updateSector(message: Message) {
        val command = Gson().fromJson(String(message.body), UpdateSectorCommand::class.java)
        val sectorOpt = sectorWriteRepository.findById(command.sectorId)
        if (!sectorOpt.isPresent) {
            // TODO: handle error
            return
        }
        val sector = sectorOpt.get()

        if (command.gid != null) {
            val gardenOpt = gardenWriteRepository.findById(command.gid)
            if (gardenOpt.isPresent) {
                sector.garden = gardenOpt.get()
            }
        }
        if (command.gfid != null) {
            val gfOpt = gardenerFlowerWriteRepository.findById(command.gfid)
            if (gfOpt.isPresent) {
                sector.gardenerFlower = gfOpt.get()
            }
        }
        // 날짜 업데이트
        if (command.date != null) {
            sector.date = LocalDate.parse(command.date)
        }

        val saved = sectorWriteRepository.save(sector)

        val doc = SectorDocument(
            id = saved.id,
            gid = saved.garden.id,
            gfid = saved.gardenerFlower.id,
            date = saved.date
        )
        syncGateway.send(doc, "sector", "upsert")
    }

    private fun deleteSector(message: Message) {
        val command = Gson().fromJson(String(message.body), DeleteSectorCommand::class.java)
        if (sectorWriteRepository.existsById(command.sectorId)) {
            sectorWriteRepository.deleteById(command.sectorId)
            syncGateway.send(mapOf("id" to command.sectorId), "sector", "delete")
        } else {
            // TODO: handle error
        }
    }
}
