package com.midas.boardservice.security.dto

import org.springframework.security.core.GrantedAuthority

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