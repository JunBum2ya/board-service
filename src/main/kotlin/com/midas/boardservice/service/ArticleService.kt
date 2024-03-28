package com.midas.boardservice.service

import com.midas.boardservice.domain.contant.ResultStatus
import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.dto.ArticleCommentDto
import com.midas.boardservice.dto.ArticleDto
import com.midas.boardservice.dto.ArticleWithCommentsDto
import com.midas.boardservice.dto.param.ArticleSearchParam
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.repository.ArticleRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArticleService(private val articleRepository: ArticleRepository) {
    private val logger = LoggerFactory.getLogger(ArticleService::class.java)
    /**
     * 동적으로 게시글 조회
     */
    @Transactional(readOnly = true)
    fun searchArticles(
        searchType: SearchType? = null,
        searchText: String? = null,
        pageable: Pageable
    ): Page<ArticleDto> {
        val page = searchText?.let {
            val param = when (searchType) {
                SearchType.TITLE -> ArticleSearchParam(title = it)
                SearchType.CONTENT -> ArticleSearchParam(content = it)
                SearchType.ID -> ArticleSearchParam(memberId = it)
                SearchType.NICKNAME -> ArticleSearchParam(nickname = it)
                SearchType.HASHTAG -> ArticleSearchParam(hashtag = listOf(it))
                null -> null
            }
            param?.let { articleRepository.searchArticles(param, pageable) } ?: articleRepository.findAll(pageable)
        } ?: articleRepository.findAll(pageable)
        return page.map { ArticleDto.from(it) }
    }

    /**
     * 댓글 포함해서 DTO에 담기
     */
    @Throws(CustomException::class)
    @Transactional(readOnly = true)
    fun getArticleWithComments(articleId: Long): ArticleWithCommentsDto {
        return articleRepository.findByIdOrNull(articleId)
            ?.let { ArticleWithCommentsDto.from(it) }
            ?:throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY,"게시글이 없습니다. - $articleId")
    }

    /**
     * 게시글 가져오기
     */
    @Throws(CustomException::class)
    @Transactional(readOnly = true)
    fun getArticle(articleId: Long): ArticleDto {
        return articleRepository.findByIdOrNull(articleId)
            ?.let { ArticleDto.from(it) }
            ?:throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY,"게시글이 없습니다. - $articleId")
    }

    /**
     * 게시글 수정
     */
    @Throws(CustomException::class)
    fun updateArticle(articleId: Long, articleData: ArticleDto) {
        try {
            val article = articleRepository.getReferenceById(articleId)
            article.update(title = articleData.title, content = articleData.content)
            article.clearHashtags()
        }catch (e: EntityNotFoundException) {
            logger.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.localizedMessage)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY,"게시글이 없습니다. - $articleId")
        }
    }

    /**
     * 게시글 삭제
     */
    fun deleteArticle(articleId: Long) {
        articleRepository.deleteById(articleId)
    }
}