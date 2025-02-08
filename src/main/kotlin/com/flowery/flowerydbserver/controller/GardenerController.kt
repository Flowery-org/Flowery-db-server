package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.constant.GardenerStatus
import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.gardener.*
import com.flowery.flowerydbserver.projection.GardenerProjection
import com.flowery.flowerydbserver.projection.UserProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gardener")
class GardenerController(
    @Autowired private val commandGateway: CommandGateway,
    @Autowired private val gardenerProjection: GardenerProjection
) {
    init {
        println("GardenerController")
    }
    @PostMapping
    fun createGardener(@RequestBody request: CreateGardenerRequest): String {

        val command = CreateGardenerCommand(
            ident = request.ident,
            password = request.password.hashCode().toString(),
            email = request.email,
            name = request.name,
            nickname = request.nickname,
            status = GardenerStatus.UNFINISHED
        )

        commandGateway.send(command,"gardener", "create")
        return "Create Gardener"
    }

    @DeleteMapping
    fun deleteGardenerById(@RequestBody request: DeleteGardenerRequest): String {

        val command = DeleteGardenerCommand(
            request.id
        )

        commandGateway.send(command, "gardener", "delete")
        return "Delete Gardener"
    }

    @PutMapping("/status")
    fun updateGardenerStatus(@RequestBody request: UpdateGardenerStatusRequest): String {

        val command = UpdateGardenerStatusCommand(
            id = request.id, // uid
            status = request.status
        )

        commandGateway.send(command,"gardener.status", "update")
        return "Update Gardener Status"
    }

    @PutMapping("/password")
    fun updateGardenerPassword(@RequestBody request: UpdateGardenerPasswordRequest): String {

        val command = UpdateGardenerPasswordCommand(
            id = request.id,
            newPassword = request.newPassword.hashCode().toString(),
        )

        commandGateway.send(command,"gardener.password", "update")
        return "Update Gardener Password"
    }

    @PutMapping("/name")
    fun updateGardenerName(@RequestBody request: UpdateGardenerNameRequest): String {

        val command = UpdateGardenerNameCommand(
            id = request.id,
            name = request.name
        )

        commandGateway.send(command,"gardener.name", "update")
        return "Update Gardener Name"
    }

    @PutMapping("/nickname")
    fun createGardenerNickname(@RequestBody request: UpdateGardenerNicknameRequest): String {

        val command = UpdateGardenerNicknameCommand(
            id = request.id,
            nickname = request.nickname
        )

        commandGateway.send(command,"gardener.nickname", "update")
        return "Update Gardener Nickname"
    }
}