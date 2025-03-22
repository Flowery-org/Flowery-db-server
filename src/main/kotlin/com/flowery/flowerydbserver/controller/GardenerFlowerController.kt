package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.request.gardenerflower.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@Tag(name = "GardenerFlower", description = "정원사의 꽃 관련 API")
@RequestMapping("/gardener-flower")
class GardenerFlowerController (
    @Autowired private val commandGateway: CommandGateway
) {
    @PutMapping("/blossom")
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
    
    // 기존 SectorController 기능 통합 - 시작
    
    @PostMapping("/garden")
    @Operation(summary = "정원사의 꽃을 정원에 배치", description = "정원사의 꽃을 특정 정원에 배치합니다 (기존 Sector 생성 기능 대체)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "정원 배치 성공"),
            ApiResponse(responseCode = "400", description = "정원 배치 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun assignGardenerFlowerToGarden(@RequestBody request: AssignGardenerFlowerToGardenRequest): ResponseEntity<Any> {
        val command = AssignGardenerFlowerToGardenCommand(
            gfid = request.gfid,  // GardenerFlower ID
            gid = request.gid,    // Garden ID
            date = request.date   // 선택적 날짜 (기본값은 createdAt 사용)
        )
        commandGateway.send(command, "gardener-flower.garden", "assign")
        return ResponseEntity.ok("GardenerFlower assigned to garden.")
    }
    
    @PutMapping("/{gfid}/garden")
    @Operation(summary = "정원사의 꽃 정원 배치 변경", description = "정원사의 꽃의 정원 배치를 변경합니다 (기존 Sector 업데이트 기능 대체)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "정원 배치 변경 성공"),
            ApiResponse(responseCode = "400", description = "정원 배치 변경 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateGardenerFlowerGarden(
        @Parameter(description = "변경할 정원사의 꽃 ID", required = true, example = "123412", `in` = ParameterIn.PATH)
        @PathVariable gfid: String,
        @RequestBody request: UpdateGardenerFlowerGardenRequest
    ): ResponseEntity<Any> {
        val command = UpdateGardenerFlowerGardenCommand(
            gfid = gfid,
            gid = request.gid,
            date = request.date
        )
        commandGateway.send(command, "gardener-flower.garden", "update")
        return ResponseEntity.ok("GardenerFlower garden assignment updated.")
    }
    
    @DeleteMapping("/{gfid}/garden")
    @Operation(summary = "정원사의 꽃 정원 배치 제거", description = "정원사의 꽃을 정원에서 제거합니다 (기존 Sector 삭제 기능 대체)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "정원 배치 제거 성공"),
            ApiResponse(responseCode = "400", description = "정원 배치 제거 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun removeGardenerFlowerFromGarden(
        @Parameter(description = "제거할 정원사의 꽃 ID", required = true, example = "123412", `in` = ParameterIn.PATH)
        @PathVariable gfid: String
    ): ResponseEntity<Any> {
        val command = RemoveGardenerFlowerFromGardenCommand(gfid)
        commandGateway.send(command, "gardener-flower.garden", "remove")
        return ResponseEntity.ok("GardenerFlower removed from garden.")
    }
    
    // 기존 SectorController 기능 통합 - 끝
}