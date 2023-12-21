package com.campus.board.service

import com.campus.board.domain.Member
import com.campus.board.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@DisplayName("비즈니스 로직 - 회원")
@ExtendWith(MockitoExtension::class)
class MemberServiceTest {
    @InjectMocks
    private lateinit var memberService: MemberService
    @Mock
    private lateinit var memberRepository: MemberRepository

    @DisplayName("존재하는 회원 아이디를 검색하면, 회원 데이터를 반환합니다.")
    @Test
    fun givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {
        //given
        val userId = "testUser"
        given(memberRepository.findById(userId)).willReturn(Optional.of(createMember(userId)))
        //when
        val memberDto = memberService.searchUser(userId)
        //then
        assertThat(memberDto).isNotNull
        then(memberRepository).should().findById(userId)
    }

    private fun createMember(userId: String): Member {
        return Member(id = userId, password = "1234", email = "test@naver.com", nickname = "testUser")
    }

    @DisplayName("존재하지 않는 회원 아이디를 검색하면, null을 반환합니다.")
    @Test
    fun givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
        //given
        val userId = "testUser"
        given(memberRepository.findById(userId)).willReturn(Optional.empty())
        //when
        val memberDto = memberService.searchUser(userId)
        //then
        assertThat(memberDto).isNull()
        then(memberRepository).should().findById(userId)
    }

}