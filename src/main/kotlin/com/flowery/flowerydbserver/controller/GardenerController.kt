package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.constant.GardenerStatus
import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.*
import com.flowery.flowerydbserver.model.document.GardenerDocument
import com.flowery.flowerydbserver.model.request.gardener.*
import com.flowery.flowerydbserver.projection.GardenerProjection
import com.flowery.flowerydbserver.projection.UserProjection
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
@Tag(name = "Gardener", description = "정원사(유저) 관련 API")
@RequestMapping("/gardener")
class GardenerController(
    @Autowired private val commandGateway: CommandGateway,
    @Autowired private val gardenerProjection: GardenerProjection
) {
    init {
        println("GardenerController")
    }
    @PostMapping
    @Operation(summary = "정원사 생성", description = "회원가입 시 정원사 생성")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "정원사 생성 성공"),
            ApiResponse(responseCode = "400", description = "정원사 생성 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun createGardener(@RequestBody request: CreateGardenerRequest): String {
        val command = CreateGardenerCommand(
            ident = request.ident,
            password = request.password.hashCode().toString(),
            email = request.email,
            name = request.name,
            nickname = request.nickname,
            status = GardenerStatus.UNFINISHED
        )

        commandGateway.send(command,"gardener", "create")
        return "Create Gardener"
    }

    @DeleteMapping
    @Operation(summary = "정원사 삭제", description = "회원탈퇴 시 정원사 삭제")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "정원사 삭제 성공"),
            ApiResponse(responseCode = "400", description = "유저 삭제 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun deleteGardenerById(@RequestBody request: DeleteGardenerRequest): String {

        val command = DeleteGardenerCommand(
            request.id
        )

        commandGateway.send(command, "gardener", "delete")
        return "Delete Gardener"
    }

    @PutMapping("/status")
    @Operation(summary = "정원사 status 변경")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "정원사 status 변경 성공"),
            ApiResponse(responseCode = "400", description = "정원사 status 변경 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateGardenerStatus(@RequestBody request: UpdateGardenerStatusRequest): String {

        val command = UpdateGardenerStatusCommand(
            id = request.id, // uid
            status = request.status
        )

        commandGateway.send(command,"gardener.status", "update")
        return "Update Gardener Status"
    }

    @PutMapping("/password")
    @Operation(summary = "정원사 비밀번호 변경")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "비밀번호 변경 성공"),
            ApiResponse(responseCode = "400", description = "비밀번호 변경 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateGardenerPassword(@RequestBody request: UpdateGardenerPasswordRequest): String {

        val command = UpdateGardenerPasswordCommand(
            id = request.id,
            newPassword = request.newPassword.hashCode().toString(),
        )

        commandGateway.send(command,"gardener.password", "update")
        return "Update Gardener Password"
    }

    @PutMapping("/name")
    @Operation(summary = "정원사 이름 변경")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "이름 변경 성공"),
            ApiResponse(responseCode = "400", description = "이름 변경 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateGardenerName(@RequestBody request: UpdateGardenerNameRequest): String {

        val command = UpdateGardenerNameCommand(
            id = request.id,
            name = request.name
        )

        commandGateway.send(command,"gardener.name", "update")
        return "Update Gardener Name"
    }

    @PutMapping("/nickname")
    @Operation(summary = "정원사 닉네임 변경")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "닉네임 변경 성공"),
            ApiResponse(responseCode = "400", description = "닉네임 변경 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun updateGardenerNickname(@RequestBody request: UpdateGardenerNicknameRequest): String {

        val command = UpdateGardenerNicknameCommand(
            id = request.id,
            nickname = request.nickname
        )

        commandGateway.send(command,"gardener.nickname", "update")
        return "Update Gardener Nickname"
    }

    @GetMapping("/{id}")
    @Operation(summary = "정원사 데이터 가져오기", description = "id로 정원사 데이터 가져오기")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "정원사 데이터 GET 성공"),
            ApiResponse(responseCode = "400", description = "정원사 데이터 GET 실패", content = [Content(schema = Schema(hidden = true))]),
            ApiResponse(responseCode = "500", description = "서버 오류", content = [Content(schema = Schema(hidden = true))])
        ]
    )
    fun getUserById(
        @Parameter(description = "정원사 ID", required = true, example = "123412", `in` = ParameterIn.PATH)
        @PathVariable id: String): GardenerDocument? {
            return this.gardenerProjection.queryGardener(id)
    }

}