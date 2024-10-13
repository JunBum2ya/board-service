package com.midas.boardservice.member.service

import com.midas.boardservice.common.component.JwtTokenProvider
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.member.dto.MemberAuthenticationDto
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.member.dto.security.JwtBoardPrincipal
import com.midas.boardservice.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    /**\
     * 회원 조회
     */
    @Transactional(readOnly = true)
    fun searchMember(memberId: String): MemberDto? {
        return memberRepository.findByIdOrNull(memberId)?.let { MemberDto.from(it) }
    }

    @Transactional(readOnly = true)
    fun login(userId: String, password: String): MemberAuthenticationDto {
        //사용자 조회
        val member = memberRepository.findByIdOrNull(userId) ?: throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        if (!passwordEncoder.matches(password, member.getPassword())) {
            //패스워드 검증
            throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
        }
        val authentication = this.authenticateMember(member)
        return authentication
    }

    @Transactional
    fun join(memberDto: MemberDto): MemberAuthenticationDto {
        if (memberRepository.findByIdOrNull(memberDto.id) != null) {
            throw CustomException(ResultStatus.DUPLICATE_UNIQUE_PROPERTY)
        }
        val member = memberRepository.save(memberDto.toEntity(password = passwordEncoder.encode(memberDto.password)))
        val authentication = this.authenticateMember(member)
        return authentication
    }

    /**
     * 회원 저장
     */
    @Transactional
    fun saveMember(memberData: MemberDto): MemberDto {
        val member = memberRepository.save(memberData.toEntity())
        return MemberDto.from(member)
    }

    private fun authenticateMember(member: Member): MemberAuthenticationDto {
        val dummyPassword = passwordEncoder.encode("{bcrypt}${UUID.randomUUID()}") //더미 패스워드 생성

        val principal = JwtBoardPrincipal(
            username = member.getId(),
            password = dummyPassword,
            nickname = member.getNickname(),
            email = member.getEmail(),
            authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        )
        //인증
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(principal, principal.password, principal.authorities)

        //access 토큰 발급
        val accessToken = jwtTokenProvider.generateToken(principal)
        
        return MemberAuthenticationDto(
            username = principal.username,
            accessToken = accessToken,
            issuedAt = LocalDateTime.now()
        )
    }
}