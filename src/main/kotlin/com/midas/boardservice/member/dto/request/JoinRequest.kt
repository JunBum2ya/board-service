package com.midas.boardservice.member.dto.request

import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.member.dto.MemberDto
import jakarta.validation.constraints.NotEmpty

class JoinRequest(
    @field:NotEmpty(message = "아이디를 입력하세요.") var username: String?,
    @field:NotEmpty(message = "패스워드를 입력하세요.") var password: String?,
    @field:NotEmpty(message = "이메일을 입력하세요.") var email: String?,
    var nickname: String?,
    var memo: String?
) {
    fun toDto(): MemberDto {
        return MemberDto(
            id = username ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            password = password ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            email = email ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            nickname = nickname ?: username ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            memo = memo
        )
    }
}