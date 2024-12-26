package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.gateway.CommandGateway
import com.flowery.flowerydbserver.model.command.CreateUserCommand
import com.flowery.flowerydbserver.model.document.UserDocument
import com.flowery.flowerydbserver.model.query.GetUserQuery
import com.flowery.flowerydbserver.model.request.CreateUserRequest
import com.flowery.flowerydbserver.projection.UserProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
    @Autowired private val commandGateway: CommandGateway,
    @Autowired private val userProjection: UserProjection
) {
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): String {
        commandGateway.send(CreateUserCommand(request.name),"user", "create")
        return "ok" //* TODO: Return Proper response
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): UserDocument? {
        return this.userProjection.queryUser(id)
    }
}