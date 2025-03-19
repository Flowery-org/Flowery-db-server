package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateSectorCommand
import com.flowery.flowerydbserver.model.command.UpdateSectorCommand
import com.flowery.flowerydbserver.model.command.DeleteSectorCommand
import com.flowery.flowerydbserver.model.request.CreateSectorRequest
import com.flowery.flowerydbserver.model.request.UpdateSectorRequest
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/sector")
class SectorController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun createSector(@RequestBody request: CreateSectorRequest): ResponseEntity<Any> {
        val command = CreateSectorCommand(
            gid = request.gid,
            gfid = request.gfid,
            date = request.date,
        )
        commandGateway.send(command, "sector", "create")
        return ResponseEntity.ok("Sector created.")
    }

    @PutMapping("/{sectorId}")
    fun updateSector(
        @PathVariable sectorId: String,
        @RequestBody request: UpdateSectorRequest
    ): ResponseEntity<Any> {
        val command = UpdateSectorCommand(
            sectorId = sectorId,
            gid = request.gid,
            gfid = request.gfid,
            date = request.date
        )
        commandGateway.send(command, "sector", "update")
        return ResponseEntity.ok("Sector updated.")
    }

    @DeleteMapping("/{sectorId}")
    fun deleteSector(@PathVariable sectorId: String): ResponseEntity<Any> {
        val command = DeleteSectorCommand(sectorId)
        commandGateway.send(command, "sector", "delete")
        return ResponseEntity.ok("Sector deleted.")
    }
}
