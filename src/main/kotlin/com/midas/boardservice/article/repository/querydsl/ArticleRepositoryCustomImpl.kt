package com.midas.boardservice.article.repository.querydsl

import com.midas.boardservice.article.domain.Article
import com.midas.boardservice.article.domain.QArticle
import com.midas.boardservice.article.domain.QHashtag
import com.midas.boardservice.article.dto.param.ArticleSearchParam
import com.querydsl.core.types.dsl.BooleanExpression
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
        val hashtag = QHashtag.hashtag
        val query = jpaQueryFactory.selectFrom(article)
            .leftJoin(article.hashtags, hashtag)
            .where(buildWhereClause(param))
        val countQuery = jpaQueryFactory.select(article.count()).leftJoin(hashtag).from(article).where(buildWhereClause(param))
        val articles = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(articles, pageable, countQuery.fetch().size.toLong())
    }

    override fun findByHashtagNames(hashtagNames: Collection<String>, pageable: Pageable): Page<Article> {
        val article = QArticle.article
        val hashtag = QHashtag.hashtag
        val query = jpaQueryFactory.selectFrom(article)
            .innerJoin(article.hashtags, hashtag)
            .where(hashtag.hashtagName.`in`(hashtagNames))
        val countQuery = jpaQueryFactory.select(article.count())
            .from(article)
            .innerJoin(article.hashtags, hashtag)
            .where(hashtag.hashtagName.`in`(hashtagNames))
        val articles = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(articles, pageable, countQuery.fetch().size.toLong())
    }

    override fun deleteArticle(articleId: Long, memberId: String) {
        val article = QArticle.article
        jpaQueryFactory.delete(article)
            .where(article.id.eq(articleId).and(article.member.id.eq(memberId)))
            .execute()
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