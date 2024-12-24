package com.flowery.flowerydbserver.config

import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore
import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.AggregateSnapshotter
import org.axonframework.eventsourcing.Snapshotter
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {
    @Bean
    fun eventStorageEngine(): EventStorageEngine {
        return InMemoryEventStorageEngine()
    }

    @Bean
    fun tokenStore(): TokenStore {
        return InMemoryTokenStore()
    }

//    @Bean
//    fun commandBus(): CommandBus {
//        return SimpleCommandBus.builder().build()
//    }
//
//    @Bean
//    fun eventBus(): EventBus {
//        return SimpleEventBus.builder().build()
//    }

}