package com.campus.board.dto.security

import com.campus.board.dto.MemberDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.annotation.meta.TypeQualifierNickname

class MemberPrincipal(
    private val username: String,
    private val password: String,
    val email: String,
    val nickname: String,
    val memo: String?
) : UserDetails {

    private val authorities: Collection<out GrantedAuthority> = setOf(SimpleGrantedAuthority(RoleType.USER.roleName))

    companion object {
        fun from(memberDto: MemberDto): MemberPrincipal {
            return MemberPrincipal(
                username = memberDto.id,
                password = memberDto.password,
                email = memberDto.email,
                nickname = memberDto.nickname,
                memo = memberDto.memo
            )
        }
    }

    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return this.authorities
    }

    override fun getPassword(): String {
        TODO("Not yet implemented")
    }

    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    enum class RoleType(val roleName: String) {
        USER(roleName = "ROLE_USER")
    }

}