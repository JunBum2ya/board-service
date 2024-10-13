package com.midas.boardservice.member.repository

import com.midas.boardservice.config.TestJpaConfig
import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@DisplayName("Member 리포지토리 테스트")
@DataJpaTest
@ActiveProfiles("testdb")
@Import(TestJpaConfig::class)
class MemberRepositoryTest(@Autowired private val memberRepository: MemberRepository) {

    private var firstMember: Member? = null

    @BeforeEach
    fun initMemberData() {
        firstMember = memberRepository.save(
            Member(
                id = "testUser1",
                email = "test1@test.com",
                nickname = "newUser",
                password = "1234"
            )
        )
        memberRepository.save(
            Member(
                id = "testUser2",
                email = "test2@test.com",
                nickname = "newUser",
                password = "1234"
            )
        )
        memberRepository.save(
            Member(
                id = "testUser3",
                email = "test3@test.com",
                nickname = "newUser",
                password = "1234"
            )
        )
    }

    @DisplayName("member를 저장하면 member가 반환된다.")
    @Test
    fun givenMember_whenSaveMember_thenReturnsMember() {
        //given
        val member = createMember()
        //when
        val savedMember = memberRepository.save(member)
        //then
        assertThat(savedMember).isNotNull
        assertThat(savedMember.getCreatedAt()).isNotNull()
        assertThat(savedMember.getUpdatedAt()).isNotNull()
        assertThat(savedMember).isEqualTo(member)
    }

    @DisplayName("중복된 이메일을 가진 member를 저장하면 예외가 발생한다.")
    @Test
    fun givenDuplicatedEmailMember_whenSaveMember_thenThrowsException() {
        //given
        val member = Member(id = "testUser4", email = "test1@test.com", nickname = "newUser", password = "1234")
        //when
        val savedMember = memberRepository.save(member)
        //then
        assertThrows<Exception> { memberRepository.flush() }
    }

    @DisplayName("pageable로 검색을 하면 Member가 페이징되어 반환된다.")
    @Test
    fun givenPageable_whenSearchMembers_thenReturnsMemberPage() {
        //given
        val pageable = Pageable.ofSize(10)
        //when
        val page = memberRepository.findAll(pageable)
        //then
        assertThat(page).isNotEmpty
        assertThat(page.size).isEqualTo(10)
        assertThat(page.totalPages).isEqualTo(1)
        assertThat(page.content.size).isEqualTo(3)
    }

    @DisplayName("member id로 member를 조회하면 member가 반환된다.")
    @Test
    fun givenMemberId_whenSearchMember_thenReturnsNullableMember() {
        //given
        val memberId = firstMember?.getId()?:"null"
        //when
        val member = memberRepository.findByIdOrNull(memberId)
        //then
        assertThat(member).isNotNull
        assertThat(member).isEqualTo(firstMember)
    }

    @DisplayName("member의 파라미터를 수정하면 실제 데이터도 수정된다.")
    @Test
    fun givenMember_whenUpdateMember_thenUpdateMember() {
        //given
        //when
        firstMember?.update(email = "new@test.com", nickname = "member", memo = "test")
        //then
        val updatedMember = memberRepository.findByIdOrNull(firstMember?.getId()?:"null")
        assertThat(firstMember).isEqualTo(updatedMember)
        assertThat(updatedMember?.getEmail()).isEqualTo("new@test.com")
    }

    @DisplayName("member의 파라미터를 중복된 email로 수정하면 예외가 발생한다.")
    @Test
    fun givenMember_whenUpdateDuplicateEmail_thenThrowsException() {
        //given
        memberRepository.save(createMember())
        //when
        firstMember?.update(email = "test@test.com", nickname = "member", memo = "test")
        //then
        assertThrows<DataIntegrityViolationException> { memberRepository.flush() }
    }

    @DisplayName("member의 아이디로 삭제를 하면 member의 총 개수가 줄어든다.")
    @Test
    fun givenMemberId_whenDeleteMember_thenReduceAllMember() {
        //given
        val memberId = firstMember?.getId()?:"null"
        //when
        memberRepository.deleteById(memberId)
        //when
        assertThat(memberRepository.findAll().size).isEqualTo(2)
    }

    private fun createMember(): Member {
        return Member(id = "testUser", email = "test@test.com", nickname = "newUser", password = "1234")
    }

}