package com.midas.boardservice.common.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.midas.boardservice.common.domain.constant.ResultStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
class CommonResponse<T>(val code: String, val message: String, val data: T? = null) {
    companion object {
        fun <T> of(status: ResultStatus, data: T): CommonResponse<T> {
            return CommonResponse(code = status.code, message = status.message, data)
        }
        fun <T> of(status: ResultStatus, message: String, data: T): CommonResponse<T> {
            return CommonResponse(code = status.code, message = message, data)
        }

        fun of(status: ResultStatus): CommonResponse<Any> {
            return CommonResponse(code = status.code, message = status.message)
        }
        fun <T> of(data: T): CommonResponse<T> {
            return CommonResponse.of(ResultStatus.SUCCESS, data)
        }
        fun of(code: String, message: String): CommonResponse<Any> {
            return CommonResponse(code = code, message = message)
        }
        fun <T> of(code: String, message: String,  data: T): CommonResponse<T> {
            return CommonResponse(code = code, message = message, data = data)
        }
    }
}