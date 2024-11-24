package com.midas.boardservice.config

import com.midas.boardservice.member.service.MemberService
import com.midas.boardservice.security.component.JwtTokenProvider
import com.midas.boardservice.security.config.SecurityConfig
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.event.annotation.BeforeTestMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@TestConfiguration
class TestSecurityConfig(
    @MockBean private val jwtTokenProvider: JwtTokenProvider,
    @MockBean private val memberService: MemberService,
    @MockBean private val clientRegistrationRegistry: InMemoryClientRegistrationRepository
) {

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

}