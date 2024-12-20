package com.midas.boardservice.article.dto.response

import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.article.dto.ArticleDto
import com.midas.boardservice.exception.CustomException
import java.time.LocalDateTime

data class ArticleResponse(
    val id: Long,
    val title: String,
    val content: String,
    val hashtags: List<String>,
    val createdAt: LocalDateTime?,
    val email: String,
    val nickname: String
) {
    companion object {
        @Throws(CustomException::class)
        fun from(articleDto: ArticleDto): ArticleResponse {
            return ArticleResponse(
                id = articleDto.articleId ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                title = articleDto.title,
                content = articleDto.content,
                hashtags = articleDto.hashtags.map { it.hashtagName }.toMutableList(),
                createdAt = articleDto.createdAt,
                email = articleDto.memberDto.email,
                nickname = articleDto.memberDto.nickname
            )
        }
    }
}