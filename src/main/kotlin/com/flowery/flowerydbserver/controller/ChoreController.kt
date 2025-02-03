package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateChoreCommand
import com.flowery.flowerydbserver.model.command.UpdateChoreCommand
import com.flowery.flowerydbserver.model.command.DeleteChoreCommand
import com.flowery.flowerydbserver.model.request.CreateChoreRequest
import com.flowery.flowerydbserver.model.request.UpdateChoreRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chore")
class ChoreController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createChore(@RequestBody request: CreateChoreRequest): ResponseEntity<Any> {
        val command = CreateChoreCommand(
            gid = request.gid,
            gfid = request.gfid,
            content = request.content
        )
        commandGateway.send(command, "chore", "create")
        return ResponseEntity.ok("Chore created.")
    }

    @PutMapping("/{choreId}")
    fun updateChore(
        @PathVariable choreId: String,
        @RequestBody request: UpdateChoreRequest
    ): ResponseEntity<Any> {
        val command = UpdateChoreCommand(
            choreId = choreId,
            content = request.content,
            finished = request.finished
        )
        commandGateway.send(command, "chore", "update")
        return ResponseEntity.ok("Chore updated.")
    }

    @DeleteMapping("/{choreId}")
    fun deleteChore(@PathVariable choreId: String): ResponseEntity<Any> {
        val command = DeleteChoreCommand(choreId)
        commandGateway.send(command, "chore", "delete")
        return ResponseEntity.ok("Chore deleted.")
    }
}
