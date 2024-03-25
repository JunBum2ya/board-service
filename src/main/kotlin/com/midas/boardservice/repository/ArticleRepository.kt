package com.midas.boardservice.repository;

import com.midas.boardservice.domain.Article
import com.midas.boardservice.repository.querydsl.ArticleRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long>,ArticleRepositoryCustom {
}