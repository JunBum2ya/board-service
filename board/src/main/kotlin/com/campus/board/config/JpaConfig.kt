package com.campus.board.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig {

    @Bean
    fun auditorAware() : AuditorAware<String> {
        return AuditorAware { Optional.of("testUser") }
    }

}