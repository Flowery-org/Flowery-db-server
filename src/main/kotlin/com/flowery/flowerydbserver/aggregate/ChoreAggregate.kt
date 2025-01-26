package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateChoreCommand
import com.flowery.flowerydbserver.model.command.DeleteChoreCommand
import com.flowery.flowerydbserver.model.command.UpdateChoreCommand
import com.flowery.flowerydbserver.model.document.ChoreDocument
import com.flowery.flowerydbserver.model.entity.ChoreEntity
import com.flowery.flowerydbserver.repository.ChoreWriteRepository
import com.flowery.flowerydbserver.repository.GardenWriteRepository
import com.flowery.flowerydbserver.repository.GardenerFlowerWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ChoreAggregate(
    private val choreWriteRepository: ChoreWriteRepository,
    private val gardenWriteRepository: GardenWriteRepository,
    private val gardenerFlowerWriteRepository: GardenerFlowerWriteRepository,
    private val syncGateway: SyncGateway
) {
    @RabbitListener(queues = [CommandQueueNameList.CHORE_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey
        when (rk) {
            "command.chore.create" -> createChore(message)
            "command.chore.update" -> updateChore(message)
            "command.chore.delete" -> deleteChore(message)
        }
    }

    private fun createChore(message: Message) {
        val command = Gson().fromJson(String(message.body), CreateChoreCommand::class.java)

        // FK 조회
        val gardenOpt = gardenWriteRepository.findById(command.gid)
        val gfOpt = gardenerFlowerWriteRepository.findById(command.gfid)
        if (!gardenOpt.isPresent || !gfOpt.isPresent) {
            // TODO handle error
            return
        }

        val newChore = ChoreEntity(
            garden = gardenOpt.get(),
            gardenerFlower = gfOpt.get(),
            content = command.content,
            finished = false,
            createdAt = LocalDate.now(),
            updatedAt = LocalDate.now()
        )
        val saved = choreWriteRepository.save(newChore)

        // Make Document
        val doc = ChoreDocument(
            id = saved.id,
            gardenId = saved.garden.id,
            gardenerFlowerId = saved.gardenerFlower.id,
            content = saved.content,
            finished = saved.finished,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
        syncGateway.send(doc, "chore", "upsert")
    }

    private fun updateChore(message: Message) {
        val command = Gson().fromJson(String(message.body), UpdateChoreCommand::class.java)
        val choreOpt = choreWriteRepository.findById(command.choreId)
        if (!choreOpt.isPresent) {
            // TODO handle error
            return
        }
        val chore = choreOpt.get()
        chore.content = command.content
        chore.finished = command.finished
        chore.updatedAt = LocalDate.now()

        val saved = choreWriteRepository.save(chore)
        val doc = ChoreDocument(
            id = saved.id,
            gardenId = saved.garden.id,
            gardenerFlowerId = saved.gardenerFlower.id,
            content = saved.content,
            finished = saved.finished,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
        syncGateway.send(doc, "chore", "upsert")
    }

    private fun deleteChore(message: Message) {
        val command = Gson().fromJson(String(message.body), DeleteChoreCommand::class.java)
        if (choreWriteRepository.existsById(command.choreId)) {
            choreWriteRepository.deleteById(command.choreId)
            syncGateway.send(mapOf("id" to command.choreId), "chore", "delete")
        } else {
            // TODO handle error
        }
    }
}