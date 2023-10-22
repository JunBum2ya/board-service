package com.campus.board.repository;

import com.campus.board.domain.ArticleComment
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleCommentRepository : JpaRepository<ArticleComment, Long> {
}