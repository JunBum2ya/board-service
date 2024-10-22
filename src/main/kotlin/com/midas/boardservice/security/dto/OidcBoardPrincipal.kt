package com.midas.boardservice.security.dto

import com.midas.boardservice.member.dto.MemberDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class OidcBoardPrincipal(
    username: String,
    authorities: MutableCollection<out GrantedAuthority>,
    @get:JvmName("getTEmail") override val email: String,
    nickname: String,
    password: String,
    memo: String? = null,
    private val claims: Map<String, Any>,
    private val attributes: Map<String, Any>,
    private val userInfo: OidcUserInfo,
    private val idToken: OidcIdToken
) : BoardPrincipal(
    username = username,
    authorities = authorities,
    email = email,
    nickname = nickname,
    password = password,
    memo = memo
), OidcUser {

    companion object {
        fun from(
            oidcUser: OidcUser,
            username: String = oidcUser.name,
            password: String,
            memo: String? = null
        ): OidcBoardPrincipal {
            return OidcBoardPrincipal(
                username = username,
                authorities = mutableListOf(SimpleGrantedAuthority("member")),
                email = oidcUser.email,
                nickname = oidcUser.nickName,
                password = password,
                memo = memo,
                claims = oidcUser.claims,
                attributes = oidcUser.attributes,
                userInfo = oidcUser.userInfo,
                idToken = oidcUser.idToken
            )
        }

        fun from(
            member: MemberDto,
            claims: Map<String, Any>,
            attributes: Map<String, Any>,
            idToken: OidcIdToken,
            userInfo: OidcUserInfo
        ): OidcBoardPrincipal {
            val authorities =
                setOf(RoleType.USER).map { roleType -> SimpleGrantedAuthority(roleType.roleName) }.toMutableSet()
            return OidcBoardPrincipal(
                username = member.id,
                password = member.password,
                authorities = authorities,
                email = member.email,
                nickname = member.nickname,
                memo = member.memo,
                claims = claims,
                attributes = attributes,
                idToken = idToken,
                userInfo = userInfo
            )
        }
    }

    override fun getName(): String {
        return this.username
    }

    override fun getAttributes(): Map<String, Any> {
        return this.attributes
    }

    override fun getClaims(): Map<String, Any> {
        return this.claims
    }

    override fun getUserInfo(): OidcUserInfo {
        return this.userInfo
    }

    override fun getIdToken(): OidcIdToken {
        return this.idToken
    }

}