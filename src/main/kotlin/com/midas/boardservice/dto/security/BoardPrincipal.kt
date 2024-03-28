package com.midas.boardservice.dto.security

import com.midas.boardservice.dto.MemberDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

data class BoardPrincipal(
    val username: String,
    val password: String,
    val authorities: Collection<out GrantedAuthority>,
    val email: String,
    val nickname: String,
    val memo: String?,
    val oAuth2Attributes: Map<String, Any>
) : UserDetails, OAuth2User {

    companion object {
        fun from(member: MemberDto): BoardPrincipal {
            val authorities = setOf(RoleType.USER).map { roleType -> SimpleGrantedAuthority(roleType.roleName) }.toSet()
            return BoardPrincipal(
                username = member.id,
                password = member.password,
                authorities = authorities,
                email = member.email,
                nickname = member.nickname,
                memo = member.memo,
                oAuth2Attributes = mapOf()
            )
        }
    }

    override fun getName(): String {
        return this.username
    }

    override fun getAttributes(): Map<String, Any> {
        return this.oAuth2Attributes
    }

    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return this.authorities
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    enum class RoleType(val roleName: String) {
        USER(roleName = "ROLE_USER")
    }

}