package com.midas.boardservice.article.dto.response

import com.midas.boardservice.article.dto.ArticleWithCommentsDto
import java.time.LocalDateTime

data class ArticleWithCommentsResponse(
    val id: Long?,
    val title: String,
    val content: String,
    val hashtags: Set<String>,
    val createdAt: LocalDateTime?,
    val email: String,
    val nickname: String,
    val userId: String,
    val articleCommentsResponse: Set<ArticleCommentResponse>
) {
    companion object {
        fun from(articleWithCommentsDto: ArticleWithCommentsDto): ArticleWithCommentsResponse {
            return ArticleWithCommentsResponse(
                id = articleWithCommentsDto.articleId,
                title = articleWithCommentsDto.title,
                content = articleWithCommentsDto.content,
                hashtags = articleWithCommentsDto.hashtags.map { it.hashtagName }.toSet(),
                createdAt = articleWithCommentsDto.createdAt,
                email = articleWithCommentsDto.memberDto.email,
                nickname = articleWithCommentsDto.memberDto.nickname,
                userId = articleWithCommentsDto.memberDto.id,
                articleCommentsResponse = articleWithCommentsDto.articleComments
                    .map { ArticleCommentResponse.from(it) }
                    .toSet()
            )
        }
    }
}