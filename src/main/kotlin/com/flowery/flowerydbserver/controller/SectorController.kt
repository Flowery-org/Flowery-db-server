package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateSectorCommand
import com.flowery.flowerydbserver.model.command.DeleteSectorCommand
import com.flowery.flowerydbserver.model.command.UpdateSectorCommand
import com.flowery.flowerydbserver.model.request.CreateSectorRequest
import com.flowery.flowerydbserver.model.request.UpdateSectorRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sector")
class SectorController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createSector(@RequestBody request: CreateSectorRequest): String {
        val command = CreateSectorCommand(
            gid = request.gid,
            fid = request.fid,
            date = request.date // 예: "2025-01-19"
        )
        commandGateway.send(command, "sector", "create")
        return "Sector created."
    }

    @PutMapping("/{id}")
    fun updateSector(
        @PathVariable id: String,
        @RequestBody request: UpdateSectorRequest
    ): String {
        val command = UpdateSectorCommand(
            id = id,
            fid = request.fid,
            date = request.date
        )
        commandGateway.send(command, "sector", "update")
        return "Sector updated."
    }

    @DeleteMapping("/{id}")
    fun deleteSector(@PathVariable id: String): String {
        val command = DeleteSectorCommand(id)
        commandGateway.send(command, "sector", "delete")
        return "Sector deleted."
    }
}