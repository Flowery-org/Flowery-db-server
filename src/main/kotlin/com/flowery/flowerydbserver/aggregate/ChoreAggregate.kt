package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateChoreCommand
import com.flowery.flowerydbserver.model.command.DeleteChoreCommand
import com.flowery.flowerydbserver.model.command.UpdateChoreCommand
import com.flowery.flowerydbserver.model.document.ChoreDocument
import com.flowery.flowerydbserver.model.entity.ChoreEntity
import com.flowery.flowerydbserver.model.entity.SectorEntity
import com.flowery.flowerydbserver.repository.ChoreWriteRepository
import com.flowery.flowerydbserver.repository.SectorWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ChoreAggregate(
    @Autowired private val choreWriteRepository: ChoreWriteRepository,
    @Autowired private val sectorWriteRepository: SectorWriteRepository,
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

        // 1) command.sid 는 Sector의 ID(문자열)일 것
        // 2) 실제 DB에서 SectorEntity 조회
        val sectorOpt = sectorWriteRepository.findById(command.sid)
        if (!sectorOpt.isPresent) {
            // TODO: Sector가 없을 경우 예외 처리
            return
        }
        val sectorEntity: SectorEntity = sectorOpt.get()

        // 3) ChoreEntity 에 sectorEntity를 넣어준다
        val newChore = ChoreEntity(
            uid = command.uid,
            sid = sectorEntity,  // ManyToOne
            fid = command.fid,
            content = command.content,
            finished = false,
            createdAt = LocalDate.now(),
            updatedAt = null
        )
        val saved = choreWriteRepository.save(newChore)

        // 4) ReadDB(Mongo) 동기화 (upsert)
        val doc = ChoreDocument(
            id = saved.id,
            uid = saved.uid,
            sid = saved.sid.id,
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

        val choreOpt = choreWriteRepository.findById(command.id)
        if (choreOpt.isPresent) {
            val chore = choreOpt.get()
            chore.content = command.content
            chore.finished = command.finished
            chore.updatedAt = LocalDate.now()

            // sector도 새로?
//             val newSectorOpt = sectorWriteRepository.findById(command.newSectorId)
//             if (newSectorOpt.isPresent) {
//                 chore.sid = newSectorOpt.get()
//             }

            val saved = choreWriteRepository.save(chore)
            val doc = ChoreDocument(
                id = saved.id,
                uid = saved.uid,
                sid = saved.sid.id,
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
        val choreOpt = choreWriteRepository.findById(command.id)
        if (choreOpt.isPresent) {
            choreWriteRepository.deleteById(command.id)
            syncGateway.send(mapOf("id" to command.id), "chore", "delete")
        } else {
            // TODO: 예외 처리
        }
    }
}