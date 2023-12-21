package com.campus.board.config

import com.campus.board.dto.security.MemberPrincipal
import com.campus.board.repository.MemberRepository
import com.campus.board.service.MemberService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.authorizeHttpRequests { o ->
            o.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        }
        httpSecurity.authorizeHttpRequests { o ->
            o.requestMatchers(HttpMethod.GET, "/articles", "search-hashtag","/api/**").permitAll()
            o.anyRequest().authenticated()
        }
        httpSecurity.formLogin {  }.logout {  }
        return httpSecurity.build()
    }


    @Bean
    fun userDetailsService(memberService: MemberService): UserDetailsService {
        return UserDetailsService { username: String ->
            memberService
                .searchUser(username)?.let { MemberPrincipal.from(it) }
                ?: throw UsernameNotFoundException("유저를 찾을 수 없습니다 - username: $username")
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

}