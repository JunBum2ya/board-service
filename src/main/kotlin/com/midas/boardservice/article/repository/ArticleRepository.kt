package com.midas.boardservice.article.repository;

import com.midas.boardservice.article.domain.Article
import com.midas.boardservice.article.repository.querydsl.ArticleRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long>, ArticleRepositoryCustom {
}