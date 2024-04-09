package com.midas.boardservice.repository.querydsl

import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.QArticle
import com.midas.boardservice.domain.QHashtag
import com.midas.boardservice.dto.param.ArticleSearchParam
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ArticleRepositoryCustomImpl(private val jpaQueryFactory: JPAQueryFactory) :
    QuerydslRepositorySupport(Article::class.java), ArticleRepositoryCustom {
    override fun searchArticles(param: ArticleSearchParam, pageable: Pageable): Page<Article> {
        val article = QArticle.article
        val query = jpaQueryFactory.selectFrom(article)
            .where(buildWhereClause(param))
        val countQuery = jpaQueryFactory.select(article.count()).from(article).where(buildWhereClause(param))
        val articles = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(articles, pageable, countQuery.fetch().size.toLong())
    }

    private fun buildWhereClause(param: ArticleSearchParam): BooleanExpression? {
        val article = QArticle.article
        val hashtag = QHashtag.hashtag
        return param.title?.let { article.title.eq(it) }
            ?.and(param.content?.let { article.content.contains(it) })
            ?.and(param.memberId?.let { article.member.id.eq(it) })
            ?.and(param.nickname?.let { article.member.nickname.eq(it) })
            ?.and(param.hashtag?.let { hashtag.hashtagName.`in`(it) })
    }
}