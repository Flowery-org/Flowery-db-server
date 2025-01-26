package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateGardenCommand
import com.flowery.flowerydbserver.model.command.DeleteGardenCommand
import com.flowery.flowerydbserver.model.command.UpdateGardenCommand
import com.flowery.flowerydbserver.model.document.GardenDocument
import com.flowery.flowerydbserver.model.entity.GardenEntity
import com.flowery.flowerydbserver.repository.GardenWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GardenAggregate(
    @Autowired private val gardenWriteRepository: GardenWriteRepository,
    @Autowired private val syncGateway: SyncGateway
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

        val newEntity = GardenEntity(
            uid = command.uid,
            key = command.key
        )
        val saved = gardenWriteRepository.save(newEntity)

        val doc = GardenDocument(
            id = saved.id,
            uid = saved.uid,
            key = saved.key
        )
        syncGateway.send(doc, "garden", "upsert")
    }

    private fun updateGarden(message: Message) {
        val command = Gson().fromJson(String(message.body), UpdateGardenCommand::class.java)
        val entityOpt = gardenWriteRepository.findById(command.id)
        if (entityOpt.isPresent) {
            val garden = entityOpt.get()
            garden.key = command.key

            val saved = gardenWriteRepository.save(garden)
            val doc = GardenDocument(
                id = saved.id,
                uid = saved.uid,
                key = saved.key
            )
            syncGateway.send(doc, "garden", "upsert")
        } else {
            // TODO: 예외 처리
        }
    }

    private fun deleteGarden(message: Message) {
        val command = Gson().fromJson(String(message.body), DeleteGardenCommand::class.java)
        val entityOpt = gardenWriteRepository.findById(command.id)
        if (entityOpt.isPresent) {
            gardenWriteRepository.deleteById(command.id)
            syncGateway.send(mapOf("id" to command.id), "garden", "delete")
        } else {
            // TODO: 예외 처리
        }
    }
}