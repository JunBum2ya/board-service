package com.midas.boardservice.article.dto.request

import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.article.dto.ArticleDto
import com.midas.boardservice.article.dto.HashtagDto
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.exception.CustomException
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import java.util.stream.Collectors

data class ArticleRequest(
    @field:Size(max = 100, min = 0, message = "제목을 다시한번 확인하세요.") val title: String?,
    @field:NotEmpty(message = "본문을 입력하세요.") val content: String?,
    val hashtags: List<String> = listOf(),
) {
    fun toDto(memberDto: MemberDto): ArticleDto {
        return ArticleDto(
            memberDto = memberDto,
            title = title ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            content = content ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            hashtags = hashtags.stream().map { HashtagDto(hashtagName = it) }.collect(Collectors.toSet())
        )
    }

    fun toHashtags(): List<HashtagDto> {
        return hashtags.map { HashtagDto(hashtagName = it) }
    }
}