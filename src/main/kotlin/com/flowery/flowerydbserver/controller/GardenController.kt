package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateGardenCommand
import com.flowery.flowerydbserver.model.command.DeleteGardenCommand
import com.flowery.flowerydbserver.model.command.UpdateGardenCommand
import com.flowery.flowerydbserver.model.request.CreateGardenRequest
import com.flowery.flowerydbserver.model.request.UpdateGardenRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/garden")
class GardenController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createGarden(@RequestBody request: CreateGardenRequest): String {
        val command = CreateGardenCommand(
            uid = request.uid,
            key = request.key
        )
        commandGateway.send(command, "garden", "create")
        return "Garden created."
    }

    @PutMapping("/{id}")
    fun updateGarden(
        @PathVariable id: String,
        @RequestBody request: UpdateGardenRequest
    ): String {
        val command = UpdateGardenCommand(
            id = id,
            key = request.key
        )
        commandGateway.send(command, "garden", "update")
        return "Garden updated."
    }

    @DeleteMapping("/{id}")
    fun deleteGarden(@PathVariable id: String): String {
        val command = DeleteGardenCommand(id)
        commandGateway.send(command, "garden", "delete")
        return "Garden deleted."
    }
}