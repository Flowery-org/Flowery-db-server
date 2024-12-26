package com.flowery.flowerydbserver.config

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SyncRmqConfig{
    @Value("exchange.sync")
    private lateinit var exchange: String

    @Bean
    fun userSyncQueue(): Queue = Queue(SyncQueueNameList.USER_QUEUE ,true)

    //* Add additional queue as tables are updated.

    @Bean
    fun syncExchange(): TopicExchange = TopicExchange(exchange)

    @Bean
    fun userSyncBinding(userSyncQueue: Queue, syncExchange: TopicExchange): Binding
        = BindingBuilder.bind(userSyncQueue).to(syncExchange).with("sync.user.#")

    @Bean
    fun syncRmqTemplate(connectionFactory: ConnectionFactory, jsonMessageConverter: Jackson2JsonMessageConverter): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter
        return rabbitTemplate
    }
}