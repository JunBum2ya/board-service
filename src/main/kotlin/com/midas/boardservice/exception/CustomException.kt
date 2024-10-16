package com.midas.boardservice.exception

import com.midas.boardservice.common.domain.constant.ResultStatus

class CustomException(val code: String, override val message: String) : RuntimeException(message) {
    constructor(status: ResultStatus) : this(code = status.code, message = status.message)
    constructor(status: ResultStatus, message: String) : this(code = status.code, message = message)
}