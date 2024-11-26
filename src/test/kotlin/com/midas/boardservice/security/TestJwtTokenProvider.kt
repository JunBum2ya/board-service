package com.midas.boardservice.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.security.component.JwtTokenProvider
import com.midas.boardservice.security.dto.JwtBoardPrincipal
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

class TestJwtTokenProvider(objectMapper: ObjectMapper) :
    JwtTokenProvider(secret = "1234", objectMapper = objectMapper, tokenValidityInSeconds = 1000L) {

    override fun afterPropertiesSet() {

    }

    override fun generateToken(principal: JwtBoardPrincipal): String {
        return "JWT-TOKEN"
    }

    override fun extractAuthentication(token: String): Authentication {
        val principal = JwtBoardPrincipal(
            username = "testUser",
            password = "testPassword",
            email = "test@test.com",
            nickname = "test",
            authorities = listOf(SimpleGrantedAuthority("TEST-USER"))
        )
        return UsernamePasswordAuthenticationToken(principal, principal.password, principal.authorities)
    }
}