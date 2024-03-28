package com.midas.boardservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class BoardServiceApplication

fun main(args: Array<String>) {
    runApplication<BoardServiceApplication>(*args)
}
