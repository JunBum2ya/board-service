package com.midas.boardservice.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@TestConfiguration
class TestJpaConfig {
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware { Optional.of("testUser") }
    }
}