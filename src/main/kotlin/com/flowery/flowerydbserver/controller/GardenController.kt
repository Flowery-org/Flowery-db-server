package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateGardenCommand
import com.flowery.flowerydbserver.model.command.UpdateGardenCommand
import com.flowery.flowerydbserver.model.command.DeleteGardenCommand
import com.flowery.flowerydbserver.model.request.CreateGardenRequest
import com.flowery.flowerydbserver.model.request.UpdateGardenRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Garden", description = "정원 관련 API")
@RequestMapping("/api/garden")
class GardenController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    @Operation(summary = "정원 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Garden 생성 성공"),
            ApiResponse(responseCode = "400", description = "Garden 생성 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun createGarden(@RequestBody request: CreateGardenRequest): ResponseEntity<Any> {
        val command = CreateGardenCommand(
            gid = request.gid,
            key = request.key
        )
        commandGateway.send(command, "garden", "create")
        return ResponseEntity.ok("Garden created.")
    }

    @PutMapping("/{gid}")
    @Operation(summary = "정원 업데이트")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Garden 업데이트 성공"),
            ApiResponse(responseCode = "400", description = "Garden 업데이트 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateGarden(
        @Parameter(description = "업데이트할 정원 ID", required = true, example = "123412", `in` = ParameterIn.PATH)
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
    @Operation(summary = "정원 삭제")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Garden 삭제 성공"),
            ApiResponse(responseCode = "400", description = "Garden 삭제 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun deleteGarden(
        @Parameter(description = "삭제할 정원 ID", required = true, example = "123412", `in` = ParameterIn.PATH)
        @PathVariable gid: String
    ): ResponseEntity<Any> {
        val command = DeleteGardenCommand(gid)
        commandGateway.send(command, "garden", "delete")
        return ResponseEntity.ok("Garden deleted.")
    }
}
