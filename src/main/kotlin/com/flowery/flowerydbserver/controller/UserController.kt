package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.command.CreateUserCommand
import com.flowery.flowerydbserver.model.request.CreateUserRequest
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/user")
class UserController(
    @Autowired private val commandGateway: CommandGateway,
    @Autowired private val queryGateway: QueryGateway
) {
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): CompletableFuture<String>{
        val uid = UUID.randomUUID()
        return commandGateway.send(CreateUserCommand(uid, request.name))
    }
}