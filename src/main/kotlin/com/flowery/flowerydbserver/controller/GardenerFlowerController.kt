package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.gardener.*
import com.flowery.flowerydbserver.model.request.gardenerflower.CreateGardenerFlowerRequest
import com.flowery.flowerydbserver.model.request.gardenerflower.UpdateGardenerFlowerBlossomRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gardenerflower")
class GardenerFlowerController (
    @Autowired private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createGardenerFlower(@RequestBody request: CreateGardenerFlowerRequest): String {

        val command = CreateGardenerFlowerCommand(
            uid = request.uid,
            fid = request.fid,
            isBlossom = false
        )

        commandGateway.send(command,"gardener_flower", "create")
        return "Create GardenerFlower"
    }

    @PutMapping
    fun updateGardenerFlowerStatus(@RequestBody request: UpdateGardenerFlowerBlossomRequest): String {

        val command = UpdateGardenerFlowerBlossomCommand (
            uid = request.uid,
            fid = request.fid,
            isBlossom = request.isBlossom
        )

        commandGateway.send(command,"gardener_flower", "update")
        return "Update GardenerFlower Blossom"
    }

    @PutMapping
    fun updateGardenerFlowerPassword(@RequestBody request: UpdateGardenerPasswordRequest): String {
        val command = UpdateGardenerPasswordCommand(
            id = request.id,
            newPassword = request.newPassword.hashCode().toString(),
        )

        commandGateway.send(command,"gardener", "create")
        return "Update Gardener Password"
    }

    @PutMapping
    fun updateGardenerFlowerName(@RequestBody request: UpdateGardenerNameRequest): String {
        val command = UpdateGardenerNameCommand(
            id = request.id,
            name = request.name
        )

        commandGateway.send(command,"gardener", "create")
        return "Update Gardener Name"
    }

    @PutMapping
    fun createGardenerFlowerNickname(@RequestBody request: UpdateGardenerNicknameRequest): String {
        val command = UpdateGardenerNicknameCommand(
            id = request.id,
            nickname = request.nickname
        )

        commandGateway.send(command,"gardener", "create")
        return "Create Gardener Nickname"
    }
}