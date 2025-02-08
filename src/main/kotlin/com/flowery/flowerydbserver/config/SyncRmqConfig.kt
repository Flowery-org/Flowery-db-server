package com.flowery.flowerydbserver.config

import com.flowery.flowerydbserver.constant.SyncQueueNameList
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SyncRmqConfig {
    @Value("exchange.sync")
    private lateinit var exchange: String

    // ----------------------------------
    // 1) Exchange
    // ----------------------------------
    @Bean
    fun syncExchange(): TopicExchange = TopicExchange(exchange)


    // ----------------------------------
    // 2) User Queue + Binding
    // ----------------------------------
    @Bean
    fun userSyncQueue(): Queue = Queue(SyncQueueNameList.USER_QUEUE, true)

    @Bean
    fun userSyncBinding(
        userSyncQueue: Queue,
        syncExchange: TopicExchange
    ): Binding = BindingBuilder.bind(userSyncQueue)
        .to(syncExchange)
        .with("sync.user.#")


    // ----------------------------------
    // 3) Chore Queue + Binding
    // ----------------------------------
    @Bean
    fun choreSyncQueue(): Queue = Queue(SyncQueueNameList.CHORE_QUEUE, true)

    @Bean
    fun choreSyncBinding(
        choreSyncQueue: Queue,
        syncExchange: TopicExchange
    ): Binding = BindingBuilder.bind(choreSyncQueue)
        .to(syncExchange)
        .with("sync.chore.#")


    // ----------------------------------
    // 4) Sector Queue + Binding
    // ----------------------------------
    @Bean
    fun sectorSyncQueue(): Queue = Queue(SyncQueueNameList.SECTOR_QUEUE, true)

    @Bean
    fun sectorSyncBinding(
        sectorSyncQueue: Queue,
        syncExchange: TopicExchange
    ): Binding = BindingBuilder.bind(sectorSyncQueue)
        .to(syncExchange)
        .with("sync.sector.#")


    // ----------------------------------
    // 5) Garden Queue + Binding
    // ----------------------------------
    @Bean
    fun gardenSyncQueue(): Queue = Queue(SyncQueueNameList.GARDEN_QUEUE, true)

    @Bean
    fun gardenSyncBinding(
        gardenSyncQueue: Queue,
        syncExchange: TopicExchange
    ): Binding = BindingBuilder.bind(gardenSyncQueue)
        .to(syncExchange)
        .with("sync.garden.#")

    @Bean
    fun gardenerSyncQueue(): Queue = Queue(SyncQueueNameList.GARDENER_QUEUE, true)

    @Bean
    fun gardenerSyncBinding(
        gardenerSyncQueue: Queue,
        syncExchange: TopicExchange
    ): Binding = BindingBuilder.bind(gardenerSyncQueue)
        .to(syncExchange)
        .with("sync.gardener.#")

    @Bean
    fun flowerSyncQueue(): Queue = Queue(SyncQueueNameList.FLOWER_QUEUE, true)

    @Bean
    fun flowerSyncBinding(
        flowerSyncQueue: Queue,
        syncExchange: TopicExchange
    ): Binding = BindingBuilder.bind(flowerSyncQueue)
        .to(syncExchange)
        .with("sync.flower.#")

    @Bean
    fun gardenerFlowerSyncQueue(): Queue = Queue(SyncQueueNameList.GARDENERFLOWER_QUEUE, true)

    @Bean
    fun gardenerFlowerSyncBinding(
        gardenerFlowerSyncQueue: Queue,
        syncExchange: TopicExchange
    ): Binding = BindingBuilder.bind(gardenerFlowerSyncQueue)
        .to(syncExchange)
        .with("sync.gardener_flower.#")


    // ----------------------------------
    // 6) RabbitTemplate & Converter
    // ----------------------------------
    @Bean
    fun syncRmqTemplate(
        connectionFactory: ConnectionFactory,
        jsonMessageConverter: Jackson2JsonMessageConverter
    ): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter
        return rabbitTemplate
    }
}