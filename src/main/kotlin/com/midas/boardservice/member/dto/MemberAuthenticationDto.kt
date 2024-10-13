package com.midas.boardservice.member.dto

import java.time.LocalDateTime

class MemberAuthenticationDto(val username: String, val accessToken: String, val issuedAt: LocalDateTime) {
}