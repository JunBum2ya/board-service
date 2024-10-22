package com.midas.boardservice.config

import com.midas.boardservice.member.service.MemberService
import com.midas.boardservice.security.config.SecurityConfig
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.test.context.event.annotation.BeforeTestMethod

@Import(SecurityConfig::class)
class TestSecurityConfig {

    @MockBean
    private lateinit var memberService: MemberService

    @MockBean
    private lateinit var clientRegistrationRepository: InMemoryClientRegistrationRepository

    @BeforeTestMethod
    fun securitySetup() {
        println("test")
    }

}