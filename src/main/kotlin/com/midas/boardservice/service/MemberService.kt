package com.midas.boardservice.service

import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MemberService(private val memberRepository: MemberRepository) {
    /**\
     * 회원 조회
     */
    @Transactional(readOnly = true)
    fun searchMember(memberId: String): MemberDto? {
        return memberRepository.findByIdOrNull(memberId)?.let { MemberDto.from(it) }
    }

    /**
     * 회원 저장
     */
    fun saveMember(memberData: MemberDto): MemberDto {
        val member = memberRepository.save(memberData.toEntity())
        return MemberDto.from(member)
    }
}