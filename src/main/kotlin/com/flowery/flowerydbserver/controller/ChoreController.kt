package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateChoreCommand
import com.flowery.flowerydbserver.model.command.DeleteChoreCommand
import com.flowery.flowerydbserver.model.command.UpdateChoreCommand
import com.flowery.flowerydbserver.model.request.CreateChoreRequest
import com.flowery.flowerydbserver.model.request.UpdateChoreRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chore")
class ChoreController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createChore(@RequestBody request: CreateChoreRequest): String {
        val command = CreateChoreCommand(
            uid = request.uid,
            gfid = request.gfid, // 이 부분을 gfid로 변경
            fid = request.fid,
            content = request.content
        )
        // "command.chore.create"
        commandGateway.send(command, "chore", "create")
        return "Chore created."
    }

    @PutMapping("/{id}")
    fun updateChore(
        @PathVariable id: String,
        @RequestBody request: UpdateChoreRequest
    ): String {
        val command = UpdateChoreCommand(
            id = id,
            content = request.content,
            finished = request.finished
        )
        // "command.chore.update"
        commandGateway.send(command, "chore", "update")
        return "Chore updated."
    }

    @DeleteMapping("/{id}")
    fun deleteChore(@PathVariable id: String): String {
        val command = DeleteChoreCommand(id)
        // "command.chore.delete"
        commandGateway.send(command, "chore", "delete")
        return "Chore deleted."
    }
}