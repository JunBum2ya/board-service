package com.midas.boardservice.repository.querydsl

import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.QArticle
import com.midas.boardservice.domain.QHashtag
import com.midas.boardservice.dto.param.ArticleSearchParam
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ArticleRepositoryCustomImpl(private val jpaQueryFactory: JPAQueryFactory) : QuerydslRepositorySupport(Article::class.java), ArticleRepositoryCustom {
    override fun searchArticles(param: ArticleSearchParam,pageable: Pageable): Page<Article> {
        val article = QArticle.article
        val hashtag = QHashtag.hashtag
        var query = jpaQueryFactory.selectFrom(article)
        param.hashtag?.let { query = query.innerJoin(article.hashtags, hashtag) }
        query = query.where(
            param.title?.let { article.title.eq(it) }
                ?.and(param.content?.let { article.content.contains(it) })
                ?.and(param.memberId?.let { article.member.id.eq(it) })
                ?.and(param.nickname?.let { article.member.nickname.eq(it) })
                ?.and(param.hashtag?.let { hashtag.hashtagName.`in`(it) })
        )
        val articles = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(articles,pageable,query.fetchCount())
    }
}