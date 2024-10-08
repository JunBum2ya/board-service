package com.midas.boardservice.member.dto.security

import com.midas.boardservice.member.dto.MemberDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

open class BoardPrincipal(
    private val username: String,
    private val password: String,
    private val authorities: Collection<out GrantedAuthority>,
    open val email: String,
    val nickname: String,
    val memo: String? = null
) : UserDetails {

    private val serialVersionUID = -6540413933065927596

    companion object {
        fun from(member: MemberDto): BoardPrincipal {
            val authorities = setOf(RoleType.USER).map { roleType -> SimpleGrantedAuthority(roleType.roleName) }.toSet()
            return BoardPrincipal(
                username = member.id,
                password = member.password,
                authorities = authorities,
                email = member.email,
                nickname = member.nickname,
                memo = member.memo
            )
        }
    }

    fun toDto(): MemberDto {
        return MemberDto(id = username, password = password, email = email, nickname = nickname, memo = memo)
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