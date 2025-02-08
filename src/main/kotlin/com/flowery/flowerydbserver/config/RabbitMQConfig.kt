import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun gardenerQueue(): Queue {
        return Queue("queue.command.gardener", true) // ✅ Durable = true (서버 재시작 후에도 유지)
    }

    @Bean
    fun gardenQueue(): Queue {
        return Queue("queue.command.garden", true)
    }

    @Bean
    fun flowerQueue(): Queue {
        return Queue("queue.command.flower", true)
    }

    @Bean
    fun choreQueue(): Queue {
        return Queue("queue.command.chore", true)
    }

    @Bean
    fun gardenerFlowerQueue(): Queue {
        return Queue("queue.command.gardener_flower", true)
    }
    @Bean
    fun sectorQueue(): Queue {
        return Queue("queue.command.sector", true)
    }

    @Bean
    fun gardenSyncQueue(): Queue {
        return Queue("queue.sync.garden", true)
    }

    @Bean
    fun gardenerSyncQueue(): Queue {
        return Queue("queue.sync.gardener", true)
    }

    @Bean
    fun flowerSyncQueue(): Queue {
        return Queue("queue.sync.flower", true)
    }

    @Bean
    fun choreSyncQueue(): Queue {
        return Queue("queue.sync.chore", true)
    }

    @Bean
    fun gardenerFlowerSyncQueue(): Queue {
        return Queue("queue.sync.gardener_flower", true)
    }

    @Bean
    fun sectorSyncQueue(): Queue {
        return Queue("queue.sync.sector", true)
    }
}
