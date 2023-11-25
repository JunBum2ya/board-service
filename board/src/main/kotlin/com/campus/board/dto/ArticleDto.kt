package com.campus.board.dto

import com.campus.board.domain.Article
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.campus.board.domain.Article}
 */
data class ArticleDto(
    val id : Long? = null,
    val title: String,
    val hashTag: String? = null,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val articleComments: MutableSet<ArticleCommentDto> = linkedSetOf()
) : Serializable {
    companion object {
        fun from(article: Article): ArticleDto {
            return ArticleDto(
                id = article.getId(),
                title = article.title,
                hashTag = article.hashTag,
                content = article.content,
                createdAt = article.getCreatedAt(),
                createdBy = article.getCreatedBy()
            )
        }
    }

    fun toEntity(): Article {
        return Article(title = this.title, hashTag = this.hashTag, content = content)
    }
}