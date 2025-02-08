package com.flowery.flowerydbserver.config

import com.flowery.flowerydbserver.constant.CommandQueueNameList
import com.rabbitmq.client.AMQP.Queue.Bind
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

    @Bean
    fun choreCommandQueue(): Queue = Queue(CommandQueueNameList.CHORE_QUEUE, true)

    @Bean
    fun sectorCommandQueue(): Queue = Queue(CommandQueueNameList.SECTOR_QUEUE, true)

    @Bean
    fun gardenCommandQueue(): Queue = Queue(CommandQueueNameList.GARDEN_QUEUE, true)

    @Bean
    fun gardenerCommandQueue(): Queue = Queue(CommandQueueNameList.GARDENER_QUEUE, true)

    @Bean
    fun flowerCommandQueue(): Queue = Queue(CommandQueueNameList.FLOWER_QUEUE, true)

    @Bean
    fun gardenerFlowerCommandQueue(): Queue = Queue(CommandQueueNameList.GARDENERFLOWER_QUEUE, true)

    //* Add additional queue as tables are updated.

    @Bean
    fun commandExchange(): TopicExchange = TopicExchange(exchange)

    @Bean
    fun userCommandBinding(userCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(userCommandQueue).to(commandExchange).with("command.user.#")

    @Bean
    fun choreCommandBinding(choreCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(choreCommandQueue).to(commandExchange).with("command.chore.#")

    @Bean
    fun sectorBinding(sectorCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(sectorCommandQueue).to(commandExchange).with("command.sector.#")

    @Bean
    fun gardenBinding(gardenCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(gardenCommandQueue).to(commandExchange).with("command.garden.#")

    @Bean
    fun gardenerBinding(gardenerCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(gardenerCommandQueue).to(commandExchange).with("command.gardener.#")

    @Bean
    fun flowerBinding(flowerCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(flowerCommandQueue).to(commandExchange).with("command.flower.#")

    @Bean
    fun gardenerFlowerBinding(gardenerFlowerCommandQueue: Queue, commandExchange: TopicExchange): Binding
        = BindingBuilder.bind(gardenerFlowerCommandQueue).to(commandExchange).with("command.gardener_flower.#")


    @Bean
    fun commandRmqTemplate(connectionFactory: ConnectionFactory, jsonMessageConverter: Jackson2JsonMessageConverter): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter
        return rabbitTemplate
    }
}