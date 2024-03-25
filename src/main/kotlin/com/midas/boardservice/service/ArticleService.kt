package com.midas.boardservice.service

import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.dto.ArticleDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArticleService {
    @Transactional(readOnly = true)
    fun searchArticles(searchType: SearchType? = null,searchText: String? = null, pageable: Pageable): Page<ArticleDto> {

    }
}