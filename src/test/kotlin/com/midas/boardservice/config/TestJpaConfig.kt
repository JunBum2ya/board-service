package com.midas.boardservice.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@EnableJpaAuditing
@TestConfiguration
class TestJpaConfig(@PersistenceContext private val entityManager: EntityManager) {
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