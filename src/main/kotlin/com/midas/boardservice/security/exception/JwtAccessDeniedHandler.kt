package com.midas.boardservice.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.common.util.HttpUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {

    private val log = LoggerFactory.getLogger(JwtAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.warn("Authentication error: ${accessDeniedException.message}")
        HttpUtil.sendData<Any>(
            response = response,
            code = ResultStatus.UNAUTHENTICATED_USER.code,
            message = ResultStatus.UNAUTHENTICATED_USER.message,
            status = HttpStatus.UNAUTHORIZED,
            objectMapper = objectMapper
        )
    }
}