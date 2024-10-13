package com.midas.boardservice.member.dto

import com.midas.boardservice.member.domain.Member
import java.time.LocalDateTime

data class MemberDto(
    val id: String,
    val password: String,
    val email: String,
    val nickname: String,
    val memo: String? = null,
    val createdAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String? = null
) {
    companion object {
        fun from(member: Member): MemberDto {
            return MemberDto(
                id = member.getId(),
                password = member.getPassword(),
                email = member.getEmail(),
                nickname = member.getNickname(),
                memo = member.getMemo(),
                createdAt = member.getCreatedAt(),
                createdBy = member.getCreatedBy(),
                updatedAt = member.getUpdatedAt(),
                updatedBy = member.getUpdatedBy()
            )
        }
    }

    fun toEntity(): Member {
        return this.toEntity(this.password)
    }

    fun toEntity(password: String): Member {
        return Member(id = id, email = email, nickname = nickname, password = password, memo = memo)
    }
}