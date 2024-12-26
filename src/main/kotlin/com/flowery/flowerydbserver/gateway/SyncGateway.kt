package com.flowery.flowerydbserver.gateway

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SyncGateway(
    @Autowired private val syncRmqTemplate: RabbitTemplate,
) {
    fun <T> send(payload: T, domain: String, action: String) {
        val rk = "sync.$domain.$action"
        syncRmqTemplate.convertAndSend("exchange.sync", rk, payload as Any)
    }
}