package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateGardenCommand
import com.flowery.flowerydbserver.model.command.UpdateGardenCommand
import com.flowery.flowerydbserver.model.command.DeleteGardenCommand
import com.flowery.flowerydbserver.model.request.CreateGardenRequest
import com.flowery.flowerydbserver.model.request.UpdateGardenRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/garden")
class GardenController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createGarden(@RequestBody request: CreateGardenRequest): ResponseEntity<Any> {
        val command = CreateGardenCommand(
            gid = request.gid,
            key = request.key
        )
        commandGateway.send(command, "garden", "create")
        return ResponseEntity.ok("Garden created.")
    }

    @PutMapping("/{gid}")
    fun updateGarden(
        @PathVariable gid: String,
        @RequestBody request: UpdateGardenRequest
    ): ResponseEntity<Any> {
        val command = UpdateGardenCommand(
            gid = gid,
            key = request.key
        )
        commandGateway.send(command, "garden", "update")
        return ResponseEntity.ok("Garden updated.")
    }

    @DeleteMapping("/{gid}")
    fun deleteGarden(@PathVariable gid: String): ResponseEntity<Any> {
        val command = DeleteGardenCommand(gid)
        commandGateway.send(command, "garden", "delete")
        return ResponseEntity.ok("Garden deleted.")
    }
}
