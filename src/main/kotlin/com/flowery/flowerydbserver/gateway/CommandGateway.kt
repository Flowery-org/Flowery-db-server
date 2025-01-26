package com.flowery.flowerydbserver.gateway

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommandGateway(
    @Autowired private val commandRmqTemplate: RabbitTemplate,
) {
    fun <T> send(payload: T, domain: String, action: String) {
        val rk = "command.$domain.$action" // 여기서 rk 생성
        commandRmqTemplate.convertAndSend("exchange.command", rk, payload as Any)
    }
}