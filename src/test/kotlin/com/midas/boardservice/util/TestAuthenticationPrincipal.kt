package com.midas.boardservice.util

import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.member.dto.security.BoardPrincipal
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class TestAuthenticationPrincipal : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType.isAssignableFrom(BoardPrincipal::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        return BoardPrincipal.from(
            MemberDto(
                id = "test",
                password = "1234",
                nickname = "test",
                email = "test@test.com"
            )
        )
    }
}