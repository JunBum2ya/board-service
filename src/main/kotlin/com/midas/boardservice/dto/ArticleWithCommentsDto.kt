package com.midas.boardservice.dto

import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.contant.ResultStatus
import com.midas.boardservice.exception.CustomException
import java.time.LocalDateTime
import kotlin.jvm.Throws

data class ArticleWithCommentsDto(
    val articleId: Long? = null,
    val memberDto: MemberDto,
    val articleComments: Set<ArticleCommentDto>,
    val title: String,
    val content: String,
    val hashtags: Set<HashtagDto> = mutableSetOf(),
    val createdAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String? = null
) {
    companion object {
        @Throws(CustomException::class)
        fun from(article: Article): ArticleWithCommentsDto {
            return ArticleWithCommentsDto(
                articleId = article.getId() ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                memberDto = MemberDto.from(article.member),
                articleComments = article.articleComments.map { ArticleCommentDto.from(it) }.toSet(),
                title = article.getTitle(),
                content = article.getContent(),
                hashtags = article.hashtags.map { HashtagDto.from(it) }.toSet(),
                createdAt = article.getCreatedAt(),
                createdBy = article.getCreatedBy(),
                updatedAt = article.getUpdatedAt(),
                updatedBy = article.getUpdatedBy()
            )
        }
    }
}