package com.flowery.flowerydbserver.aggregate

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateGardenerFlowerCommand
import com.flowery.flowerydbserver.model.command.DeleteGardenerFlowerCommand
import com.flowery.flowerydbserver.model.command.UpdateGardenerFlowerBlossomCommand
import com.flowery.flowerydbserver.model.document.GardenerFlowerDocument
import com.flowery.flowerydbserver.model.entity.GardenerFlowerEntity
import com.flowery.flowerydbserver.repository.FlowerWriteRepository
import com.flowery.flowerydbserver.repository.GardenerFlowerWriteRepository
import com.flowery.flowerydbserver.repository.GardenerWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class GardenerFlowerAggregate(
    @Autowired private val gardenerFlowerWriteRepository: GardenerFlowerWriteRepository,
    @Autowired private val gardenerWriteRepository: GardenerWriteRepository,
    @Autowired private val flowerWriteRepository: FlowerWriteRepository,
    @Autowired private val syncGateway: SyncGateway
) {
    @RabbitListener(queues = [CommandQueueNameList.GARDENERFLOWER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey

        when(rk) {
            "command.gardener_flower.create" -> createGardenerFlower(message)
            "command.gardener_flower.update" -> updateGardenerFlower(message)
            "command.gardener_flower.delete" -> deleteGardenerFlower(message)
        }
    }

    private fun createGardenerFlower(message: Message) {
        val command = Gson().fromJson(
            String(message.body),
            CreateGardenerFlowerCommand::class.java
        )


        val gardenerOpt = gardenerWriteRepository.findById(command.uid);
        val flowerOpt = flowerWriteRepository.findById(command.fid);
        if (!gardenerOpt.isPresent) {
            return
            // "Error"
        }
        if (!flowerOpt.isPresent) {
            return
            // "Error"
        }

        val en = GardenerFlowerEntity (
            gardener = gardenerOpt.get(),
            flower = flowerOpt.get(),
            isBlossom = false
        )

        var savedEntity : GardenerFlowerEntity? = null;

        try {
            savedEntity = gardenerFlowerWriteRepository.save(en)
        } catch (e: Exception){
            //* TODO: Need to add exception class
        }

        if(savedEntity != null) {
            val doc = GardenerFlowerDocument(
                id = savedEntity.id,
                gardenerId = savedEntity.gardener.id,
                flowerId = savedEntity.flower.id,
                createdAt = savedEntity.createdAt,
                updatedAt =  savedEntity.updatedAt,
                isBlossom = savedEntity.isBlossom
            )

            this.syncGateway.send(doc, "gardener_flower", "upsert")
        }
    }

    private fun updateGardenerFlower(message: Message) {
        val command = Gson().fromJson(
            String(message.body),
            UpdateGardenerFlowerBlossomCommand::class.java
        )

        val gardener = gardenerWriteRepository.findById(command.uid);
        val flower = flowerWriteRepository.findById(command.fid);
        val gardenerFlower = gardenerFlowerWriteRepository.findByGardenerAndFlower(gardener.get(), flower.get());

        if (gardenerFlower == null) {
            // Error Handling
        } else {
            gardenerFlower.isBlossom = command.isBlossom
            gardenerFlower.updatedAt = LocalDate.now()
            gardenerFlowerWriteRepository.save(gardenerFlower)

            val doc = GardenerFlowerDocument(
                id = gardenerFlower.id,
                gardenerId = gardenerFlower.gardener.id,
                flowerId = gardenerFlower.flower.id,
                createdAt = gardenerFlower.createdAt,
                updatedAt = gardenerFlower.updatedAt,
                isBlossom = gardenerFlower.isBlossom
            )

            syncGateway.send(doc, "gardener_flower", "upsert")
        }
    }

    private fun deleteGardenerFlower(message: Message) {
        val command = Gson().fromJson(
            String(message.body),
            DeleteGardenerFlowerCommand::class.java
        )

        val gardener = gardenerWriteRepository.findById(command.uid);
        val flower = flowerWriteRepository.findById(command.fid);

        if(gardenerFlowerWriteRepository.existsByGardenerAndFlower(gardener.get(),flower.get())) {
            gardenerFlowerWriteRepository.deleteByGardenerAndFlower(gardener.get(),flower.get())
            syncGateway.send(mapOf(
                "uid" to command.uid ,
                "fid" to command.fid
            ), "gardener_flower", "delete")
        } else {
            // TODO: 예외 처리
        }

    }
}