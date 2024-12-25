package com.flowery.flowerydbserver.controller

import com.flowery.flowerydbserver.model.command.CreateUserCommand
import com.flowery.flowerydbserver.model.entity.UserEntity
import com.flowery.flowerydbserver.model.request.CreateUserRequest
import com.flowery.flowerydbserver.repository.UserWriteRepository
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
    @Autowired private val writeRepository: UserWriteRepository
) {
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): String {
        val uid = UUID.randomUUID().toString()
        //this.writeRepository.save(UserEntity().apply { request.name })
        return "ok"
        //return commandGateway.send(CreateUserCommand(uid, request.name))
    }
}