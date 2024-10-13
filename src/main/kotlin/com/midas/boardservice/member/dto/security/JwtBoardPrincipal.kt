package com.midas.boardservice.member.dto.security

import com.midas.boardservice.member.dto.MemberDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtBoardPrincipal(
    username: String,
    password: String,
    authorities: Collection<out GrantedAuthority>,
    email: String,
    nickname: String
) :
    BoardPrincipal(
        username = username,
        password = password,
        authorities = authorities,
        email = email,
        nickname = nickname
    )