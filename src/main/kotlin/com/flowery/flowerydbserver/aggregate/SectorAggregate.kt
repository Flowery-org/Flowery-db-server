package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateSectorCommand
import com.flowery.flowerydbserver.model.command.UpdateSectorCommand
import com.flowery.flowerydbserver.model.command.DeleteSectorCommand
import com.flowery.flowerydbserver.model.document.SectorDocument
import com.flowery.flowerydbserver.model.entity.FlowerEntity
import com.flowery.flowerydbserver.model.entity.GardenerFlowerEntity
import com.flowery.flowerydbserver.model.entity.SectorEntity
import com.flowery.flowerydbserver.repository.SectorWriteRepository
import com.flowery.flowerydbserver.repository.FlowerWriteRepository
import com.flowery.flowerydbserver.repository.GardenerFlowerWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SectorAggregate(
    private val sectorWriteRepository: SectorWriteRepository,
    private val flowerWriteRepository: FlowerWriteRepository,
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

        val flowerOpt = flowerWriteRepository.findById(command.fid)
        val gfOpt = gardenerFlowerWriteRepository.findById(command.gfid)
        if (!flowerOpt.isPresent || !gfOpt.isPresent) {
            // TODO: error
            return
        }
        val dateParsed = command.date?.let { LocalDate.parse(it) } ?: LocalDate.now()

        val newSector = SectorEntity(
            flower = flowerOpt.get(),
            gardenerFlower = gfOpt.get(),
            date = dateParsed
        )
        val saved = sectorWriteRepository.save(newSector)

        val doc = SectorDocument(
            id = saved.id,
            fid = saved.flower.id,
            gfid = saved.gardenerFlower.id,
            date = saved.date
        )
        syncGateway.send(doc, "sector", "upsert")
    }

    private fun updateSector(message: Message) {
        val command = Gson().fromJson(String(message.body), UpdateSectorCommand::class.java)
        val sectorOpt = sectorWriteRepository.findById(command.sectorId)
        if (!sectorOpt.isPresent) {
            // TODO: error
            return
        }
        val sector = sectorOpt.get()

        if (command.fid != null) {
            val flowerOpt = flowerWriteRepository.findById(command.fid)
            if (flowerOpt.isPresent) {
                sector.flower = flowerOpt.get()
            }
        }
        if (command.gfid != null) {
            val gfOpt = gardenerFlowerWriteRepository.findById(command.gfid)
            if (gfOpt.isPresent) {
                sector.gardenerFlower = gfOpt.get()
            }
        }
        if (command.date != null) {
            sector.date = LocalDate.parse(command.date)
        }

        val saved = sectorWriteRepository.save(sector)

        val doc = SectorDocument(
            id = saved.id,
            fid = saved.flower.id,
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
            // TODO: error
        }
    }
}
