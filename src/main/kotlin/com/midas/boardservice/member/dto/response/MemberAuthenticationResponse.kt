package com.midas.boardservice.member.dto.response

import com.midas.boardservice.member.dto.MemberAuthenticationDto
import java.time.LocalDateTime

data class MemberAuthenticationResponse(
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: LocalDateTime
) {
    companion object {
        fun from(authentication: MemberAuthenticationDto): MemberAuthenticationResponse {
            return MemberAuthenticationResponse(
                username = authentication.username,
                accessToken = authentication.accessToken,
                refreshToken = "",
                expiresIn = authentication.issuedAt
            )
        }
    }
}