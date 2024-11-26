package com.midas.boardservice.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.member.service.MemberService
import com.midas.boardservice.security.component.JwtAuthenticationEntryPoint
import com.midas.boardservice.security.component.JwtTokenProvider
import com.midas.boardservice.security.config.SecurityConfig
import com.midas.boardservice.security.exception.JwtAccessDeniedHandler
import com.midas.boardservice.security.filter.JwtTokenFilter
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.BeforeTest
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.event.annotation.BeforeTestMethod

@TestConfiguration
@Import(SecurityConfig::class)
class TestSecurityConfig(
    @Autowired private val objectMapper: ObjectMapper,
    @MockkBean private val memberService: MemberService,
    @MockkBean private val clientRegistrationRepository: ClientRegistrationRepository
) {

    @Bean
    fun jwtTokenProvider(): JwtTokenProvider {
        return TestJwtTokenProvider(objectMapper)
    }

    @Bean
    fun jwtAuthenticationEntryPoint(): JwtAuthenticationEntryPoint {
        return JwtAuthenticationEntryPoint(objectMapper)
    }

    @Bean
    fun jwtAccessDeniedHandler(): JwtAccessDeniedHandler {
        return JwtAccessDeniedHandler(objectMapper)
    }

    @BeforeTestMethod
    fun securitySetUp() {

    }

}