package com.campus.board.repository;

import com.campus.board.domain.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long> {
}