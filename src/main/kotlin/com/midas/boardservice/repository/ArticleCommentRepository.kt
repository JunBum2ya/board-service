package com.midas.boardservice.repository;

import com.midas.boardservice.domain.ArticleComment
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleCommentRepository : JpaRepository<ArticleComment, Long> {
}