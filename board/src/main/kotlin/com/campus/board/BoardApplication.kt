package com.campus.board

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class BoardApplication

fun main(args: Array<String>) {
    runApplication<BoardApplication>(*args)
}
