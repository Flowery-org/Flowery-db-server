package com.flowery.flowerydbserver.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager

@Configuration
class JpaConfig {
    @Bean
    fun transactionManager(emf: EntityManagerFactory): JpaTransactionManager {
        return JpaTransactionManager(emf)
    }
}