package com.campus.board.repository;

import com.campus.board.domain.Article
import com.campus.board.repository.querydsl.ArticleRepositoryCustom
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface ArticleRepository : JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>,ArticleRepositoryCustom {
    fun findArticleByContentContaining(content : String,pageable: Pageable) : Page<Article> //본문으로 검색
    fun findArticleByTitleContaining(title : String,pageable: Pageable) : Page<Article> //제목으로 검색
    fun findArticleByCreatedBy(createdBy: String, pageable: Pageable) : Page<Article> //닉네임으로 검색
}