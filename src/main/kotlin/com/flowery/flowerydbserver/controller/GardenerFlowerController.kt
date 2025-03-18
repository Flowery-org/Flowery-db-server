package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.gardenerflower.UpdateGardenerFlowerBlossomRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "GardenerFlower", description = "정원사의 꽃 관련 API")
@RequestMapping("/gardener-flower")
class GardenerFlowerController (
    @Autowired private val commandGateway: CommandGateway
) {
    @PutMapping
    @Operation(summary = "꽃 상태 업데이트")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "꽃 상태 업데이트 성공"),
            ApiResponse(responseCode = "400", description = "꽃 상태 업데이트 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateGardenerFlowerStatus(@RequestBody request: UpdateGardenerFlowerBlossomRequest): String {
        val command = UpdateGardenerFlowerBlossomCommand (
            uid = request.uid,
            fid = request.fid,
            isBlossom = request.isBlossom
        )

        commandGateway.send(command,"gardener-flower.isBlossom", "update")
        return "Update GardenerFlower Blossom"
    }
}