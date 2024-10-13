package com.midas.boardservice.dto.request

import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.dto.ArticleCommentDto
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.exception.CustomException
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ArticleCommentRequest(
    @field:NotNull(message = "게시글 아이디를 입력하세요.") val articleId: Long?,
    val parentCommentId: Long? = null,
    @field:NotEmpty(message = "게시글 본문을 입력하세요.") val content: String?
) {

    @Throws(CustomException::class)
    fun toDto(memberDto: MemberDto): ArticleCommentDto {
        return ArticleCommentDto(
            articleId = articleId ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            parentCommentId = parentCommentId,
            content = content ?: throw CustomException(ResultStatus.NOT_VALID_REQUEST),
            member = memberDto
        )
    }

}