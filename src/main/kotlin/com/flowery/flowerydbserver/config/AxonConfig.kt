package com.flowery.flowerydbserver.config

import org.axonframework.config.ConfigurerModule
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.SnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.axonframework.serialization.json.JacksonSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfig {
    @Bean
    fun configureAxon(): ConfigurerModule {
        return ConfigurerModule { configurer ->
            configurer.configureSerializer {
                JacksonSerializer.builder().build()
            }
        }
    }

    @Bean
    fun snapshotTriggerDefinition(snapshotter: Snapshotter): SnapshotTriggerDefinition {
        return EventCountSnapshotTriggerDefinition(snapshotter, 5)
    }

}