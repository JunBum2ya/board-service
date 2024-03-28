package com.midas.boardservice.dto.response

import com.midas.boardservice.dto.ArticleCommentDto
import java.time.LocalDateTime

data class ArticleCommentResponse(
    val id: Long?,
    val content: String,
    val createdAt: LocalDateTime?,
    val email: String,
    val nickname: String,
    val userId: String,
    val parentCommentId: Long? = null,
    val childComments: Set<ArticleCommentResponse>? = null
) {
    companion object {
        fun from(articleCommentDto: ArticleCommentDto): ArticleCommentResponse {
            return ArticleCommentResponse(
                id = articleCommentDto.id,
                content = articleCommentDto.content,
                createdAt = articleCommentDto.createdAt,
                email = articleCommentDto.member.email,
                nickname = articleCommentDto.member.nickname,
                userId = articleCommentDto.member.id,
                parentCommentId = articleCommentDto.parentCommentId
            )
        }
    }

    fun hasParentComment(): Boolean {
        return parentCommentId != null
    }
}