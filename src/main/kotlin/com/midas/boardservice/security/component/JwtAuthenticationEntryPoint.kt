package com.midas.boardservice.security.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.common.dto.response.CommonResponse
import com.midas.boardservice.common.util.HttpUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class JwtAuthenticationEntryPoint(private val objectMapper: ObjectMapper): AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.warn("Authentication error: ${authException.message}")
        HttpUtil.sendData<Any>(
            response = response,
            code = ResultStatus.UNAUTHENTICATED_USER.code,
            message = ResultStatus.UNAUTHENTICATED_USER.message,
            status = HttpStatus.UNAUTHORIZED,
            objectMapper = objectMapper
        )
    }
}