package com.midas.boardservice.dto

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.member.dto.security.KakaoOAuth2Response
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import io.kotest.property.arbitrary.TypeReference

class KakaoOAuth2ResponseTest : StringSpec({
    val mapper = ObjectMapper()

    "인증 결과를 Map(deserialized json)으로 받으면, 카카오 인증 응답 객체로 변환한다." {
        val serializedResponse = """
                {
                    "id": 1234567890,
                    "connected_at": "2022-01-02T00:12:34Z",
                    "properties": {
                        "nickname": "홍길동"
                    },
                    "kakao_account": {
                        "profile_nickname_needs_agreement": false,
                        "profile": {
                            "nickname": "홍길동"
                        },
                        "has_email": true,
                        "email_needs_agreement": false,
                        "is_email_valid": true,
                        "is_email_verified": true,
                        "email": "test@gmail.com"
                    }
                }
                """.trimIndent()
        val attributes = mapper.readValue(serializedResponse, Map::class.java)
        val response = KakaoOAuth2Response.from(attributes as Map<String,Any>)
        response.id shouldBe 1234567890L
        response.kakaoAccount.profile.nickname shouldBe "홍길동"
    }
})