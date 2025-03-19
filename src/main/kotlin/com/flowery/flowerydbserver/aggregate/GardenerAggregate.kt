package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.constant.GardenerStatus
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.document.GardenerDocument
import com.flowery.flowerydbserver.model.entity.GardenerEntity
import com.flowery.flowerydbserver.repository.GardenerWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class GardenerAggregate(
    @Autowired private val gardenerWriteRepository: GardenerWriteRepository,
    @Autowired private val syncGateway: SyncGateway
) {
    @RabbitListener(queues = [CommandQueueNameList.GARDENER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey

        when(rk) {
            "command.gardener.create" -> createGardener(message)
            "command.gardener.delete" -> deleteGardener(message)
            "command.gardener.status.update" -> updateGardener(message,"status")
            "command.gardener.password.update" -> updateGardener(message,"pasword")
            "command.gardener.name.update" -> updateGardener(message,"name")
            "command.gardener.nickname.update" -> updateGardener(message,"nickname")
        }
    }
    private fun createGardener(message: Message) {
        val command = Gson().fromJson(
                String(message.body),
                CreateGardenerCommand::class.java
        )

        // 중복 확인 로직
        if (gardenerWriteRepository.existsByIdent(command.ident)) {
            // throw DuplicateGardenerException("Ident '${command.ident}' already exists.")
        }
        if (gardenerWriteRepository.existsByEmail(command.email)) {
            // throw DuplicateGardenerException("Email '${command.email}' already exists.")
        }
        if (gardenerWriteRepository.existsByNickname(command.nickname)) {
            // throw DuplicateGardenerException("Nickname '${command.nickname}' already exists.")
        }

        val newGardener = GardenerEntity(
            ident = command.ident,
            password = command.password,
            email = command.email,
            name = command.name,
            nickname = command.nickname,
            status = command.status
        )

        var savedEntity: GardenerEntity? = null;

        try {
            savedEntity = gardenerWriteRepository.save(newGardener)

            if(savedEntity != null) {
                val doc = GardenerDocument(
                    id = savedEntity.id,
                    ident = savedEntity.ident,
                    password = savedEntity.password,
                    email = savedEntity.email,
                    name = savedEntity.name,
                    nickname = savedEntity.nickname,
                    status = savedEntity.status
//                    createdAt = savedEntity.createdAt,
//                    updatedAt = savedEntity.updatedAt
                )

                this.syncGateway.send(doc, "gardener", "upsert")
            }
        } catch (e: Exception) {
            println(e.message)
            //* TODO: Need to add exception class
        }
    }

    private fun updateGardener(message: Message , delData : String) {
        val command: UpdateGardenerCommand = when (delData) {
            "status" -> Gson().fromJson(String(message.body), UpdateGardenerStatusCommand::class.java)
            "password" -> Gson().fromJson(String(message.body), UpdateGardenerPasswordCommand::class.java)
            "name" -> Gson().fromJson(String(message.body), UpdateGardenerNameCommand::class.java)
            "nickname" -> Gson().fromJson(String(message.body), UpdateGardenerNicknameCommand::class.java)
            else -> throw IllegalArgumentException("Invalid update data type")
        }

        val gardenerOpt = gardenerWriteRepository.findById(command.id)

        if (!gardenerOpt.isPresent) {
            // Error Handling
        }

        val gardener = gardenerOpt.get();

        when (command) {

            is UpdateGardenerStatusCommand -> {
                gardener.status = GardenerStatus.valueOf(command.status)
            }
            is UpdateGardenerPasswordCommand -> {
                gardener.password = command.newPassword
            }
            is UpdateGardenerNameCommand -> {
                gardener.name = command.name
            }
            is UpdateGardenerNicknameCommand -> {
                gardener.nickname = command.nickname
            }
        }

        gardener.updatedAt = LocalDate.now()
        gardenerWriteRepository.save(gardener);

        val doc = GardenerDocument(
            id = gardener.id,
            ident = gardener.ident,
            password = gardener.password,
            email = gardener.email,
            name = gardener.name,
            nickname = gardener.nickname,
            token = gardener.token,
            status = gardener.status,
//            createdAt = gardener.createdAt,
//            updatedAt = gardener.updatedAt
        )

        syncGateway.send(doc, "gardener", "upsert")
    }

    private fun deleteGardener(message: Message) {
        val command = Gson().fromJson(
            String(message.body),
            DeleteGardenerCommand::class.java
        )

        if(gardenerWriteRepository.existsById(command.id)) {
            gardenerWriteRepository.deleteById(command.id)
            syncGateway.send(mapOf("id" to command.id), "gardener", "delete")
        } else {
            // TODO: 예외 처리
        }
    }
}