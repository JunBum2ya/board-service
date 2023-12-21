package com.campus.board.dto.response

import com.campus.board.dto.ArticleDto
import java.time.LocalDateTime

class ArticleResponse(
    val id: Long?,
    val title: String,
    val content: String,
    val hashTag: String = "",
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(articleDto: ArticleDto): ArticleResponse {
            return ArticleResponse(
                id = articleDto.id,
                title = articleDto.title,
                content = articleDto.content,
                createdAt = articleDto.createdAt
            )
        }
    }
}