package com.midas.boardservice.config

import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.member.dto.security.BoardPrincipal
import com.midas.boardservice.member.dto.security.KakaoOAuth2Response
import com.midas.boardservice.member.dto.security.OAuth2BoardPrincipal
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.member.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*

@Configuration
class SecurityConfig {

    @Bean
    fun securityChain(
        httpSecurity: HttpSecurity,
        oAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>
    ): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    PathRequest.toStaticResources().atCommonLocations()
                ).permitAll()
                    .requestMatchers("/api/members/login", "/api/members/join").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            }
        }
    }

    @Bean
    fun userDetailsService(memberService: MemberService): UserDetailsService {
        return UserDetailsService { username ->
            memberService.searchMember(username)
                ?.let { BoardPrincipal.from(it) }
                ?: throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY, "유저를 찾을 수 없습니다 - username: $username")
        }
    }

    @Bean
    fun oAuth2UserService(
        memberService: MemberService,
        @Autowired passwordEncoder: PasswordEncoder
    ): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        val delegate = DefaultOAuth2UserService()

        return OAuth2UserService<OAuth2UserRequest, OAuth2User> { userRequest ->
            val oAuth2User = delegate.loadUser(userRequest)
            val kakaoResponse: KakaoOAuth2Response = KakaoOAuth2Response.from(oAuth2User.attributes)
            val registrationId = userRequest.clientRegistration.registrationId
            val providerId: String = java.lang.String.valueOf(kakaoResponse.id)
            val username = registrationId + "_" + providerId
            val dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID())
            memberService.searchMember(username)
                ?.let { OAuth2BoardPrincipal.from(it) }
                ?: OAuth2BoardPrincipal.from(
                    memberService.saveMember(
                        MemberDto(
                            id = username,
                            password = dummyPassword,
                            email = kakaoResponse.kakaoAccount.email,
                            nickname = kakaoResponse.kakaoAccount.profile.nickname
                        )
                    )
                )
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}

