package com.flowery.flowerydbserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class FloweryDbServerApplication

fun main(args: Array<String>) {
    runApplication<FloweryDbServerApplication>(*args)
}
