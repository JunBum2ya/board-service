package com.midas.boardservice.config

import com.midas.boardservice.member.service.MemberService
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.event.annotation.BeforeTestMethod

@Import(SecurityConfig::class)
class TestSecurityConfig {

    @MockBean
    private lateinit var memberService: MemberService

    @BeforeTestMethod
    fun securitySetup() {
        println("test")
    }

}