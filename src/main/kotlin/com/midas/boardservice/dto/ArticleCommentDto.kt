package com.midas.boardservice.dto

import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.ArticleComment
import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.member.dto.MemberDto
import java.time.LocalDateTime
import kotlin.jvm.Throws

data class ArticleCommentDto(
    val id: Long? = null,
    val articleId: Long,
    val member: MemberDto,
    val parentCommentId: Long? = null,
    val content: String,
    val createdAt: LocalDateTime? = null,
    val createdBy: String? = null,
    val updatedAt: LocalDateTime? = null,
    val updatedBy: String? = null,
) {
    companion object {
        @Throws(CustomException::class)
        fun from(articleComment: ArticleComment): ArticleCommentDto {
            return ArticleCommentDto(id = articleComment.getId(),
                articleId = articleComment.article.getId() ?: throw CustomException(ResultStatus.USE_NOT_PERSIST_ENTITY),
                member = MemberDto.from(articleComment.member),
                parentCommentId = articleComment.getParentCommentId(),
                content = articleComment.getContent(),
                createdAt = articleComment.getCreatedAt(),
                createdBy = articleComment.getCreatedBy(),
                updatedAt = articleComment.getUpdatedAt(),
                updatedBy = articleComment.getUpdatedBy()
            )
        }
    }

    fun toEntity(article: Article, member: Member): ArticleComment {
        return ArticleComment(article = article, member = member, content = content)
    }
}