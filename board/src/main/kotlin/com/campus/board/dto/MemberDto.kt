package com.campus.board.dto

import com.campus.board.domain.Member
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.campus.board.domain.Member}
 */
data class MemberDto(
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val createdBy: String = "",
    val modifiedAt: LocalDateTime = LocalDateTime.now(),
    val modifiedBy: String = "",
    val id: String,
    val password: String,
    val email: String,
    val nickname: String,
    val memo: String? = null
) {
    companion object {
        fun from(member: Member): MemberDto {
            return MemberDto(
                id = member.getId(),
                password = member.password,
                email = member.email,
                nickname = member.nickname,
                memo = member.memo,
                createdAt = member.getCreatedAt(),
                createdBy = member.getCreatedBy(),
                modifiedAt = member.getModifiedAt(),
                modifiedBy = member.getModifiedBy()
            )
        }
    }
}