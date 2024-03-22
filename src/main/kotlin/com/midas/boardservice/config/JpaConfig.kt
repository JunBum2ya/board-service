package com.midas.boardservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.Optional

@EnableJpaAuditing
@Configuration
class JpaConfig {
    /**
     * 현재 작업 중인 사용자 정보
     */
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware { Optional.of("testUser") }
    }
}