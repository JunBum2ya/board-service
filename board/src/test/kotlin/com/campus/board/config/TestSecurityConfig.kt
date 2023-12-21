package com.campus.board.config

import com.campus.board.dto.MemberDto
import com.campus.board.service.MemberService
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.event.annotation.BeforeTestMethod

@Import(SecurityConfig::class)
class TestSecurityConfig {
    @MockBean
    private lateinit var memberService: MemberService

    @BeforeTestMethod
    fun securitySetUp() {
        given(memberService.searchUser(anyString())).willReturn(createMemberDto())
    }

    private fun createMemberDto(): MemberDto {
        return MemberDto(id = "testUser", password = "12345678", email = "test@tistory.com", nickname = "testUser")
    }

}