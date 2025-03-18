package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateChoreCommand
import com.flowery.flowerydbserver.model.command.UpdateChoreCommand
import com.flowery.flowerydbserver.model.command.DeleteChoreCommand
import com.flowery.flowerydbserver.model.request.CreateChoreRequest
import com.flowery.flowerydbserver.model.request.UpdateChoreRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Habit", description = "할일 관련 API")
@RequestMapping("/api/chore")
class ChoreController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    @Operation(summary = "할일 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Habit 생성 성공"),
            ApiResponse(responseCode = "400", description = "Habit 생성 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
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
    @Operation(summary = "할일 업데이트")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Habit 업데이트 성공"),
            ApiResponse(responseCode = "400", description = "Habit 업데이트 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateChore(

        @Parameter(description = "업데이트할 할일 ID", required = true, example = "123412", `in` = ParameterIn.PATH)
        @PathVariable choreId: String,

        @Valid
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

    @Operation(summary = "할일 삭제")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Habit 삭제 성공"),
            ApiResponse(responseCode = "400", description = "Habit 삭제 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    @DeleteMapping("/{choreId}")
    fun deleteChore(
        @Parameter(description = "삭제할 할일 ID", example = "123412", `in` = ParameterIn.PATH)
        @PathVariable choreId: String): ResponseEntity<Any> {

        val command = DeleteChoreCommand(choreId)
        commandGateway.send(command, "chore", "delete")
        return ResponseEntity.ok("Chore deleted.")
    }
}
