package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.gardener.*
import com.flowery.flowerydbserver.model.request.gardenerflower.CreateGardenerFlowerRequest
import com.flowery.flowerydbserver.model.request.gardenerflower.UpdateGardenerFlowerBlossomRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gardener-flower")
class GardenerFlowerController (
    @Autowired private val commandGateway: CommandGateway
) {
    @PutMapping
    fun updateGardenerFlowerStatus(@RequestBody request: UpdateGardenerFlowerBlossomRequest): String {

        val command = UpdateGardenerFlowerBlossomCommand (
            uid = request.uid,
            fid = request.fid,
            isBlossom = request.isBlossom
        )

        commandGateway.send(command,"gardener-flower.isBlossom", "update")
        return "Update GardenerFlower Blossom"
    }
}