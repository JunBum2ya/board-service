package com.midas.boardservice.member.controller

import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.common.dto.response.CommonResponse
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.member.dto.request.JoinRequest
import com.midas.boardservice.member.dto.request.LoginRequest
import com.midas.boardservice.member.dto.response.MemberAuthenticationResponse
import com.midas.boardservice.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberApiController(private val memberService: MemberService) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<CommonResponse<MemberAuthenticationResponse>> {
        val authentication = memberService.login(
            userId = request.username ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            password = request.password ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST)
        )
        return ResponseEntity.ok(CommonResponse.of(MemberAuthenticationResponse.from(authentication)))
    }

    @PostMapping("/join")
    fun join(@Valid @RequestBody request: JoinRequest): ResponseEntity<CommonResponse<MemberAuthenticationResponse>> {
        val authentication = memberService.join(request.toDto())
        return ResponseEntity.ok(CommonResponse.of(MemberAuthenticationResponse.from(authentication)))
    }

}