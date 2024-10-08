package com.midas.boardservice.member.dto.security

import com.midas.boardservice.member.dto.MemberDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

/**
 * OAuth2BoardPrincipal 객체 생성
 */
class OAuth2BoardPrincipal(
    username: String,
    authorities: MutableCollection<out GrantedAuthority>,
    email: String,
    nickname: String,
    password: String,
    memo: String? = null,
    private val oAuth2Attributes: MutableMap<String, Any>
) : BoardPrincipal(
    username = username,
    authorities = authorities,
    email = email,
    nickname = nickname,
    password = password,
    memo = memo
), OAuth2User {

    companion object {
        fun from(member: MemberDto): OAuth2BoardPrincipal {
            val authorities = setOf(RoleType.USER).map { roleType -> SimpleGrantedAuthority(roleType.roleName) }.toMutableSet()
            return OAuth2BoardPrincipal(
                username = member.id,
                password = member.password,
                authorities = authorities,
                email = member.email,
                nickname = member.nickname,
                memo = member.memo,
                oAuth2Attributes = mutableMapOf()
            )
        }
    }

    override fun getName(): String {
        return this.username
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return this.oAuth2Attributes
    }

}