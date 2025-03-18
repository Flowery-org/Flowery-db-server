package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.constant.FlowerColor
import com.flowery.flowerydbserver.constant.FlowerKind
import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.flower.CreateFlowerRequest
import com.flowery.flowerydbserver.model.request.flower.DeleteFlowerRequest
import com.flowery.flowerydbserver.projection.FlowerProjection
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Flower", description = "꽃 관련 API")
@RequestMapping("/api/flower")
class FlowerController(
    @Autowired private val commandGateway: CommandGateway,
    @Autowired private val flowerProjection: FlowerProjection
) {
    @PostMapping
    @Operation(summary = "꽃 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Flower 생성 성공"),
            ApiResponse(responseCode = "400", description = "Flower 생성 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun createFlower(@RequestBody request: CreateFlowerRequest): String {

        val kind = FlowerKind.random()
        val color = FlowerColor.random()

        val command = CreateFlowerCommand(
            color = color,
            kind = kind,
            content = kind.content,
            uid = request.uid // gardenerID
        )

        commandGateway.send(command,"flower", "create")
        return "Create Flower"
    }

    @DeleteMapping
    @Operation(summary = "꽃 삭제")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Flower 삭제 성공"),
            ApiResponse(responseCode = "400", description = "Flower 삭제 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun deleteFlower(@RequestBody request: DeleteFlowerRequest): String {

        val command = DeleteFlowerCommand(
            uid = request.uid,
            fid = request.fid
        )

        commandGateway.send(command,"flower", "delete")
        return "Delete Flower"
    }
}