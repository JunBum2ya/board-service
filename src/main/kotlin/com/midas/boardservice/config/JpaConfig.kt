package com.midas.boardservice.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.Optional

@EnableJpaAuditing
@Configuration
class JpaConfig(@PersistenceContext private val entityManager: EntityManager) {
    /**
     * 현재 작업 중인 사용자 정보
     */
    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware { Optional.of("testUser") }
    }

    /**
     * querydsl 연동
     */
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}