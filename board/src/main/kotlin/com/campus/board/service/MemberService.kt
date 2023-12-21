package com.campus.board.service

import com.campus.board.dto.MemberDto
import com.campus.board.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(private val memberRepository: MemberRepository) {

    fun searchUser(username : String) : MemberDto? {
        return memberRepository.findById(username).map { MemberDto.from(it) }.orElse(null)
    }

}