package com.midas.boardservice.article.repository;

import com.midas.boardservice.article.domain.ArticleComment
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleCommentRepository : JpaRepository<ArticleComment, Long> {

    fun deleteByIdAndMember_Id(id: Long, memberId: String): Long
}