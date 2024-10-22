package com.midas.boardservice.security.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.security.dto.JwtBoardPrincipal
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.access-token-validity-in-seconds}") tokenValidityInSeconds: Long,
    private val objectMapper: ObjectMapper
) : InitializingBean {

    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    private val AUTHORITIES_KEY = "auth"
    private val tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    private var key: Key? = null

    override fun afterPropertiesSet() {
        val keyBytes = Base64.getDecoder().decode(secret.toByteArray())
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(principal: JwtBoardPrincipal): String {
        return Jwts.builder()
            .setSubject(principal.username)
            .claim(AUTHORITIES_KEY, principal.authorities.map { it.authority })
            .claim("username", principal.username)
            .claim("password", principal.password)
            .claim("email", principal.email)
            .claim("nickname", principal.nickname)
            .claim("details", principal)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(Date(Date().time + tokenValidityInMilliseconds))
            .compact()
    }

    fun extractAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJwt(token)
            .body
        val principal = JwtBoardPrincipal(
            username = claims.subject,
            password = claims["password"].toString(),
            email = claims["email"].toString(),
            nickname = claims["nickname"].toString(),
            authorities = (claims[AUTHORITIES_KEY] as List<*>).map { SimpleGrantedAuthority(it.toString()) }
        )
        return UsernamePasswordAuthenticationToken(principal, principal.password, principal.authorities)
    }

}