package com.midas.boardservice.service

import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.repository.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class MemberServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val memberService = MemberService(memberRepository)

    Given("회원 ID가 주어졌을 때") {
        val memberId = "testUser"
        When("회원이 존재한다면") {
            every { memberRepository.findByIdOrNull(any()) }.returns(createMember())
            val member = memberService.searchMember(memberId)
            Then("member가 반환된다.") {
                verify { memberRepository.findByIdOrNull(any()) }
                member shouldNotBe null
            }
        }
        When("회원이 존재하지 않는다면") {
            every { memberRepository.findByIdOrNull(any()) }.returns(null)
            val member = memberService.searchMember(memberId)
            Then("null이 반환된다.") {
                verify { memberRepository.findByIdOrNull(any()) }
                member shouldBe null
            }
        }
    }
    Given("회원 정보를 입력되었을 때") {
        val memberData = MemberDto(id = "test1", password = "3234", email = "test@test.com", nickname = "test")
        every { memberRepository.save(any()) }.returns(createMember())
        When("회원정보를 저장하여 가입시킬 경우") {
            val member = memberService.saveMember(memberData)
            Then("MemberDto가 반환된다.") {
                verify { memberRepository.save(any()) }
                member.id shouldBe "test"
                member.password shouldBe "1234"
                member.email shouldBe "test@test.com"
                member.nickname shouldBe "test"
                member.memo shouldBe "test"
            }
        }
    }
}) {
    companion object {
        fun createMember(): Member {
            return Member(id = "test", password = "1234", email = "test@test.com", nickname = "test", memo = "test")
        }
    }
}