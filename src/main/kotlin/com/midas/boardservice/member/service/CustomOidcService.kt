package com.midas.boardservice.member.service

import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.member.dto.security.OidcBoardPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

@Service
class CustomOidcService(
    private val memberService: MemberService,
    private val passwordEncoder: PasswordEncoder
) : OidcUserService() {
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = super.loadUser(userRequest)
        val registrationId = userRequest.clientRegistration.registrationId
        val username = registrationId + "_" + userRequest.idToken.preferredUsername
        val dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID())
        return memberService.searchMember(username)?.let {
            OidcBoardPrincipal.from(
                member = it,
                claims = oidcUser.claims,
                attributes = oidcUser.attributes,
                userInfo = oidcUser.userInfo,
                idToken = oidcUser.idToken
            )
        } ?: OidcBoardPrincipal.from(
            member = memberService.saveMember(
                MemberDto(
                    id = username,
                    password = dummyPassword,
                    email = oidcUser.email,
                    nickname = oidcUser.preferredUsername
                )
            ),
            attributes = oidcUser.attributes,
            claims = oidcUser.claims,
            userInfo = oidcUser.userInfo,
            idToken = oidcUser.idToken
        )
    }
}