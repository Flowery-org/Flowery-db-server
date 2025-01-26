package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateGardenCommand
import com.flowery.flowerydbserver.model.command.DeleteGardenCommand
import com.flowery.flowerydbserver.model.command.UpdateGardenCommand
import com.flowery.flowerydbserver.model.document.GardenDocument
import com.flowery.flowerydbserver.model.entity.GardenEntity
import com.flowery.flowerydbserver.repository.GardenWriteRepository
import com.flowery.flowerydbserver.repository.GardenerWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class GardenAggregate(
    private val gardenWriteRepository: GardenWriteRepository,
    private val gardenerWriteRepository: GardenerWriteRepository,
    private val syncGateway: SyncGateway
) {
    @RabbitListener(queues = [CommandQueueNameList.GARDEN_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "command.garden.create" -> createGarden(message)
            "command.garden.update" -> updateGarden(message)
            "command.garden.delete" -> deleteGarden(message)
        }
    }

    private fun createGarden(message: Message) {
        val command = Gson().fromJson(String(message.body), CreateGardenCommand::class.java)
        val gardenerOpt = gardenerWriteRepository.findById(command.gardenerId)
        if (!gardenerOpt.isPresent) {
            // handle error
            return
        }
        val newGarden = GardenEntity(
            gardener = gardenerOpt.get(),
            key = command.key
        )
        val saved = gardenWriteRepository.save(newGarden)

        val doc = GardenDocument(
            id = saved.id,
            gardenerId = saved.gardener.id,
            key = saved.key
        )
        syncGateway.send(doc, "garden", "upsert")
    }

    private fun updateGarden(message: Message) {
        val command = Gson().fromJson(String(message.body), UpdateGardenCommand::class.java)
        val gardenOpt = gardenWriteRepository.findById(command.gardenId)
        if (!gardenOpt.isPresent) {
            // handle error
            return
        }
        val garden = gardenOpt.get()
        garden.key = command.key
        val saved = gardenWriteRepository.save(garden)

        val doc = GardenDocument(
            id = saved.id,
            gardenerId = saved.gardener.id,
            key = saved.key
        )
        syncGateway.send(doc, "garden", "upsert")
    }

    private fun deleteGarden(message: Message) {
        val command = Gson().fromJson(String(message.body), DeleteGardenCommand::class.java)
        if (gardenWriteRepository.existsById(command.gardenId)) {
            gardenWriteRepository.deleteById(command.gardenId)
            syncGateway.send(mapOf("id" to command.gardenId), "garden", "delete")
        } else {
            // handle error
        }
    }
}