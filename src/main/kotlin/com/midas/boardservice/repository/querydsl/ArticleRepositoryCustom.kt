package com.midas.boardservice.repository.querydsl

import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.dto.param.ArticleSearchParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ArticleRepositoryCustom {
    fun searchArticles(param: ArticleSearchParam, pageable: Pageable): Page<Article>
    fun findByHashtagNames(hashtagNames: Collection<String>, pageable: Pageable): Page<Article>
}