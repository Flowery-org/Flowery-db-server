package com.flowery.flowerydbserver.handler.event


import com.flowery.flowerydbserver.repository.UserWriteRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserEventHandler(
    @Autowired private val userWriteRepository: UserWriteRepository
) {
    //* Handles User Created Event

}