package com.flowery.flowerydbserver.config

import com.rabbitmq.client.Channel
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter
import org.axonframework.extensions.amqp.eventhandling.DefaultAMQPMessageConverter
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource
import org.axonframework.serialization.Serializer
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonRabbitMQConfig {

    companion object {
        const val QUEUE_NAME = "axon-events"
        const val EXCHANGE_NAME = "axon-exchange"
        private val logger = LoggerFactory.getLogger(AxonRabbitMQConfig::class.java)
    }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin  = RabbitAdmin(connectionFactory)

    @Bean
    fun queue(): Queue = Queue(QUEUE_NAME, true)

    @Bean
    fun exchange(): FanoutExchange = FanoutExchange(EXCHANGE_NAME, true, false)

    @Bean
    fun binding(queue: Queue, exchange: FanoutExchange): Binding = BindingBuilder.bind(queue).to(exchange)

    @Bean
    fun amqpMessageConverter(serializer: Serializer): AMQPMessageConverter {
        return DefaultAMQPMessageConverter.builder()
            .serializer(serializer)
            .build()
    }

    @Bean
    fun springAMQPMessageSource(
        messageConverter: AMQPMessageConverter
    ): SpringAMQPMessageSource {
        return object : SpringAMQPMessageSource(messageConverter) {
            @RabbitListener(queues = [QUEUE_NAME])
            override fun onMessage(message: Message?, channel: Channel?) {
                super.onMessage(message, channel)
            }
        }
    }

    @Bean
    fun messageListenerContainer(
        connectionFactory: ConnectionFactory,
        messageSource: SpringAMQPMessageSource,
        rabbitAdmin: RabbitAdmin,
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer(connectionFactory)
        container.setQueueNames(QUEUE_NAME)
        container.setMessageListener(messageSource)
        container.setAmqpAdmin(rabbitAdmin)
        container.setErrorHandler { err ->
            logger.error("AMQP Error: ${err.message}", err)
        }
        container.setPrefetchCount(1)
        container.acknowledgeMode = AcknowledgeMode.AUTO
        return container
    }
}