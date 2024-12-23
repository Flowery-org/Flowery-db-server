package com.flowery.flowerydbserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FloweryDbServerApplication

fun main(args: Array<String>) {
    runApplication<FloweryDbServerApplication>(*args)
}
