package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateChoreCommand
import com.flowery.flowerydbserver.model.command.DeleteChoreCommand
import com.flowery.flowerydbserver.model.command.UpdateChoreCommand
import com.flowery.flowerydbserver.model.document.ChoreDocument
import com.flowery.flowerydbserver.model.entity.ChoreEntity
import com.flowery.flowerydbserver.repository.ChoreWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ChoreAggregate(
    @Autowired private val choreWriteRepository: ChoreWriteRepository,
    @Autowired private val syncGateway: SyncGateway
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

        val newEntity = ChoreEntity(
            uid = command.uid,
            sid = command.sid,
            fid = command.fid,
            content = command.content,
            finished = false,
            createdAt = LocalDate.now(),
            updatedAt = null
        )
        val saved = choreWriteRepository.save(newEntity)

        val doc = ChoreDocument(
            id = saved.id,
            uid = saved.uid,
            sid = saved.sid,
            fid = saved.fid,
            content = saved.content,
            finished = saved.finished,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
        syncGateway.send(doc, "chore", "upsert")
    }

    private fun updateChore(message: Message) {
        val command = Gson().fromJson(String(message.body), UpdateChoreCommand::class.java)
        val entityOpt = choreWriteRepository.findById(command.id)
        if (entityOpt.isPresent) {

            // 여기서 update
            val chore = entityOpt.get()
            chore.content = command.content
            chore.finished = command.finished
            chore.updatedAt = LocalDate.now()

            // Update 된 chore을 writeDB에 save
            val saved = choreWriteRepository.save(chore)
            val doc = ChoreDocument(
                id = saved.id,
                uid = saved.uid,
                sid = saved.sid,
                fid = saved.fid,
                content = saved.content,
                finished = saved.finished,
                createdAt = saved.createdAt,
                updatedAt = saved.updatedAt
            )
            syncGateway.send(doc, "chore", "upsert")
        } else {
            // TODO: 예외 처리
        }
    }

    private fun deleteChore(message: Message) {
        val command = Gson().fromJson(String(message.body), DeleteChoreCommand::class.java)
        val entityOpt = choreWriteRepository.findById(command.id)
        if (entityOpt.isPresent) {
            choreWriteRepository.deleteById(command.id)
            syncGateway.send(mapOf("id" to command.id), "chore", "delete")
        } else {
            // TODO: 예외 처리
        }
    }
}