package com.midas.boardservice.member.dto.request

import jakarta.validation.constraints.NotEmpty

data class LoginRequest(
    @field:NotEmpty(message = "아이디를 입력하세요.") var username: String?,
    @field:NotEmpty(message = "패스워드를 입력하세요.") var password: String?
)