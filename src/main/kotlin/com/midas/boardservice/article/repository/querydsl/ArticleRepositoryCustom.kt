package com.midas.boardservice.article.repository.querydsl

import com.midas.boardservice.article.domain.Article
import com.midas.boardservice.article.dto.param.ArticleSearchParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ArticleRepositoryCustom {
    fun searchArticles(param: ArticleSearchParam, pageable: Pageable): Page<Article>
    fun findByHashtagNames(hashtagNames: Collection<String>, pageable: Pageable): Page<Article>
    fun deleteArticle(articleId: Long, memberId: String)
}