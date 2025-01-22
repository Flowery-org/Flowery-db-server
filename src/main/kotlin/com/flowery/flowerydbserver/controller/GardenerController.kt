package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.gardener.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gardener")
class GardenerController(
    @Autowired private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createGardender(@RequestBody request: CreateGardenerRequest): String {

        val command = CreateGardenerCommand(
            ident = request.ident,
            password = request.password.hashCode().toString(),
            email = request.email,
            name = request.name,
            nickname = request.nickname
        )

        commandGateway.send(command,"gardener", "create")
        return "Create Gardener"
    }

    @DeleteMapping
    fun deleteGardenerById(@RequestBody request: DeleteGardenerRequest): String {
        commandGateway.send(DeleteGardenerCommand(request.id), "gardener", "delete")
        return "Delete Gardener"
    }

    @PutMapping
    fun updateGardenderStatus(@RequestBody request: UpdateGardenerStatusRequest): String {

        val command = UpdateGardenerStatusCommand(
            id = request.id,
            status = request.status
        )

        commandGateway.send(command,"gardener", "create")
        return "Update Gardener Status"
    }

    @PutMapping
    fun updateGardenderPassowrd(@RequestBody request: UpdateGardenerPasswordRequest): String {
        val command = UpdateGardenerPasswordCommand(
            id = request.id,
            newPassword = request.newPassword.hashCode().toString(),
        )

        commandGateway.send(command,"gardener", "create")
        return "Update Gardener Password"
    }

    @PutMapping
    fun updateGardenderName(@RequestBody request: UpdateGardenerNameRequest): String {
        val command = UpdateGardenerNameCommand(
            id = request.id,
            name = request.name
        )

        commandGateway.send(command,"gardener", "create")
        return "Update Gardener Name"
    }

    @PutMapping
    fun createGardenderNickname(@RequestBody request: UpdateGardenerNicknameRequest): String {
        val command = UpdateGardenerNicknameCommand(
            id = request.id,
            nickname = request.nickname
        )

        commandGateway.send(command,"gardener", "create")
        return "Create Gardener Nickname"
    }
}