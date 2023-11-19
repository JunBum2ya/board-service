package com.campus.board.service

import com.campus.board.domain.Article
import com.campus.board.dto.ArticleDto
import com.campus.board.repository.ArticleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArticleService(val articleRepository: ArticleRepository) {

    /**
     * 게시글 추가
     */
    fun saveArticle(articleDto: ArticleDto) : ArticleDto {
        val article = articleRepository.save(articleDto.toEntity())
        return ArticleDto.from(article)
    }

}