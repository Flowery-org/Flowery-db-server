package com.flowery.flowerydbserver.config

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommandRmqConfig{
    @Value("exchange.command")
    private lateinit var exchange: String

    @Bean
    fun userCommandQueue(): Queue = Queue(CommandQueueNameList.USER_QUEUE ,true)

    //* Add additional queue as tables are updated.

    @Bean
    fun commandExchange(): TopicExchange = TopicExchange(exchange)

    @Bean
    fun userCommandBinding(userCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(userCommandQueue).to(commandExchange).with("command.user.#")


    @Bean
    fun commandRmqTemplate(connectionFactory: ConnectionFactory, jsonMessageConverter: Jackson2JsonMessageConverter): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter
        return rabbitTemplate
    }
}