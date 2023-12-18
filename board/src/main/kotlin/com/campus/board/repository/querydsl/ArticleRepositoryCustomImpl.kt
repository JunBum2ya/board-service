package com.campus.board.repository.querydsl

import com.campus.board.domain.Article
import com.campus.board.domain.QArticle
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ArticleRepositoryCustomImpl() : QuerydslRepositorySupport(Article::class.java), ArticleRepositoryCustom {
    override fun findAllDistinctHashTags(): List<String> {
        return listOf()
    }
}