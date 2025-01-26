package com.flowery.flowerydbserver.controller

import FlowerEntity
import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.flower.CreateFlowerRequest
import com.flowery.flowerydbserver.model.request.flower.DeleteFlowerRequest
import com.flowery.flowerydbserver.projection.FlowerProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/flower")
class FlowerController(
    @Autowired private val commandGateway: CommandGateway,
    @Autowired private val flowerProjection: FlowerProjection
) {
    @PostMapping
    fun createFlower(@RequestBody request: CreateFlowerRequest): String {

        val kind = FlowerEntity.Kind.random()
        val color = FlowerEntity.FlowerColor.random()

        val command = CreateFlowerCommand(
            color = color,
            kind = kind,
            content = kind.content
        )

        commandGateway.send(command,"flower", "create")
        return "Create Flower"
    }

    @DeleteMapping
    fun deleteFlower(@RequestBody request: DeleteFlowerRequest): String {

        val command = DeleteFlowerCommand(
            id = request.id
        )

        commandGateway.send(command,"flower", "delete")
        return "Delete Flower"
    }
}