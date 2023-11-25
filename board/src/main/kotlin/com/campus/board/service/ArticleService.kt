package com.campus.board.service

import com.campus.board.domain.Article
import com.campus.board.dto.ArticleDto
import com.campus.board.dto.ArticleSearchParam
import com.campus.board.exception.CustomException
import com.campus.board.repository.ArticleRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArticleService(val articleRepository: ArticleRepository) {

    private val logger = LoggerFactory.getLogger(ArticleService::class.java)

    /**
     * page 검색
     */
    @Transactional(readOnly = true)
    fun searchArticles(articleSearchParam: ArticleSearchParam) : Page<ArticleDto> {
        return Page.empty()
    }

    /**
     * 게시글 추가
     */
    fun createArticle(articleDto: ArticleDto) : ArticleDto {
        val article = articleRepository.save(articleDto.toEntity())
        return ArticleDto.from(article)
    }

    /**
     * 게시글 수정
     */
    fun updateArticle(articleId : Long,articleDto: ArticleDto) : ArticleDto {
        try {
            val article = articleRepository.getReferenceById(articleId)
            article.title = articleDto.title
            article.content = articleDto.content
            article.hashTag = articleDto.hashTag
            return ArticleDto.from(article)
        }catch (e : EntityNotFoundException) {
            logger.error("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}",e.localizedMessage)
            throw CustomException(message = e.message?:"")
        }
    }

    /**
     * 게시글 삭제
     */
    fun deleteArticle(articleId: Long) {
        articleRepository.deleteById(articleId)
    }

}