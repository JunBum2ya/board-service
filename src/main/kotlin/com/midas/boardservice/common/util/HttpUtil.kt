package com.midas.boardservice.common.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.midas.boardservice.common.dto.response.CommonResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class HttpUtil {
    companion object {
        fun <T> sendData(
            response: HttpServletResponse,
            data: T? = null,
            code: String,
            message: String,
            status: HttpStatus = HttpStatus.OK,
            objectMapper: ObjectMapper
        ) {
            response.status = status.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"

            val responseData = CommonResponse.of(code = code, message = message, data = data)

            response.writer.write(objectMapper.writeValueAsString(responseData))
            response.writer.flush()

        }
    }
}
