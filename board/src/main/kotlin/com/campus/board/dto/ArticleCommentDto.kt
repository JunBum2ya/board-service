package com.campus.board.dto

import com.campus.board.domain.Article
import com.campus.board.domain.ArticleComment
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.campus.board.domain.ArticleComment}
 */
data class ArticleCommentDto(
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val createdBy: String = "",
    val content: String = ""
) : Serializable {
    companion object {
        fun from(articleComment: ArticleComment): ArticleCommentDto {
            return ArticleCommentDto(
                content = articleComment.content,
                createdAt = articleComment.getCreatedAt(),
                createdBy = articleComment.getCreatedBy()
            )
        }
    }

    fun toEntity(article: Article): ArticleComment {
        return ArticleComment(article = article, content = this.content)
    }
}