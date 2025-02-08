package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.constant.FlowerColor
import com.flowery.flowerydbserver.constant.FlowerKind
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

        val kind = FlowerKind.random()
        val color = FlowerColor.random()

        val command = CreateFlowerCommand(
            color = color,
            kind = kind,
            content = kind.content,
            uid = request.uid // gardenerID
        )

        commandGateway.send(command,"flower", "create")
        return "Create Flower"
    }

    @DeleteMapping
    fun deleteFlower(@RequestBody request: DeleteFlowerRequest): String {

        val command = DeleteFlowerCommand(
            uid = request.uid,
            fid = request.fid
        )

        commandGateway.send(command,"flower", "delete")
        return "Delete Flower"
    }
}