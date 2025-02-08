package com.flowery.flowerydbserver.aggregate
import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.gateway.SyncGateway
import com.flowery.flowerydbserver.model.command.CreateFlowerCommand
import com.flowery.flowerydbserver.model.command.CreateGardenerFlowerCommand
import com.flowery.flowerydbserver.model.command.DeleteFlowerCommand
import com.flowery.flowerydbserver.model.command.DeleteGardenerFlowerCommand
import com.flowery.flowerydbserver.model.document.FlowerDocument
import com.flowery.flowerydbserver.model.entity.FlowerEntity
import com.flowery.flowerydbserver.model.entity.GardenerFlowerEntity
import com.flowery.flowerydbserver.repository.FlowerWriteRepository
import com.flowery.flowerydbserver.repository.GardenerWriteRepository
import com.google.gson.Gson
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class FlowerAggregate(
    @Autowired private val flowerWriteRepository: FlowerWriteRepository,
    @Autowired private val gardenerWriteRepository: GardenerWriteRepository,
    @Autowired private val syncGateway: SyncGateway,
    @Autowired private val commandGateway: CommandGateway
) {
    @RabbitListener(queues = [CommandQueueNameList.FLOWER_QUEUE])
    fun on(message: Message) {
        val rk = message.messageProperties.receivedRoutingKey

        when(rk) {
            "command.flower.create" -> createFlower(message)
            "command.flower.delete" -> deleteFlower(message)
        }
    }

    private fun createFlower(message: Message) {
        val command = Gson().fromJson(
            String(message.body),
            CreateFlowerCommand::class.java
        )

        var flower = flowerWriteRepository.findByColorAndKind(
            color = command.color,
            kind = command.kind
        )

        // flower의 존재 유무 확인
        if(flower == null) {

            val newFlower = FlowerEntity(
                color = command.color,
                kind = command.kind,
                content = command.content
            )

            flower = flowerWriteRepository.save(newFlower)
        }

        // Flower Document
        val doc = FlowerDocument(
            id = flower.id,
            kind = flower.kind,
            color = flower.color,
            content = flower.content
        )
        syncGateway.send(doc,"flower","upsert")

        // gardenerFlower 생성
        val gardener = gardenerWriteRepository.findById(command.uid).get()
        val gardenerFlower = GardenerFlowerEntity(
            gardener = gardener,
            flower = flower,
        )

        val gfCommand = CreateGardenerFlowerCommand(
            uid = gardener.id, // gardenerID
            fid = flower.id,
            isBlossom = false
        )

        commandGateway.send(command,"gardener_flower", "create");
    }

    private fun deleteFlower(message: Message) {
        val command = Gson().fromJson(
            String(message.body),
            DeleteFlowerCommand::class.java
        )

        if(!flowerWriteRepository.existsById(command.fid)) {

            flowerWriteRepository.deleteById(command.fid)
            syncGateway.send(mapOf("id" to command.fid),"flower","delete")
        } else {
            // Error Handling
        }

        val gfCommand = DeleteGardenerFlowerCommand (
            uid = command.uid,
            fid = command.fid
        )

        commandGateway.send(command,"gardener_flower", "delete");
    }
}