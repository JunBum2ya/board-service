package com.midas.boardservice.service

import com.midas.boardservice.domain.Hashtag
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.dto.ArticleDto
import com.midas.boardservice.dto.ArticleWithCommentsDto
import com.midas.boardservice.dto.param.ArticleSearchParam
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.repository.ArticleRepository
import com.midas.boardservice.member.repository.MemberRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val memberRepository: MemberRepository,
    private val hashtagService: HashtagService
) {
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
                SearchType.HASHTAG -> ArticleSearchParam(hashtag = it.split(" "))
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
            ?: throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY, "게시글이 없습니다. - $articleId")
    }

    /**
     * 게시글 가져오기
     */
    @Throws(CustomException::class)
    @Transactional(readOnly = true)
    fun getArticle(articleId: Long): ArticleDto {
        return articleRepository.findByIdOrNull(articleId)
            ?.let { ArticleDto.from(it) }
            ?: throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY, "게시글이 없습니다. - $articleId")
    }

    @Transactional(readOnly = true)
    fun getArticleCount(): Long {
        return articleRepository.count()
    }

    /**
     * 게시글 저장
     */
    @Throws(CustomException::class)
    fun saveArticle(articleDto: ArticleDto): ArticleDto {
        try {
            val member = memberRepository.getReferenceById(articleDto.memberDto.id)
            val hashtags = renewHashtagsFromContent(articleDto.content)
            val article = articleDto.toEntity(member)
            article.addHashtags(hashtags)
            return ArticleDto.from(articleRepository.save(article))
        } catch (e: EntityNotFoundException) {
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    /**
     * 게시글 수정
     */
    @Throws(CustomException::class)
    fun updateArticle(articleId: Long, articleData: ArticleDto) {
        try {
            val article = articleRepository.getReferenceById(articleId)
            if(article.member.getId() == articleData.memberDto.id) {
                article.update(title = articleData.title, content = articleData.content)
                val hashtagIds = article.hashtags.mapNotNull { it.getId() }
                article.clearHashtags()
                articleRepository.flush()

                hashtagIds.forEach { hashtagService.deleteHashtagWithoutArticles(it) }

                val hashtags = renewHashtagsFromContent(articleData.content)
                article.addHashtags(hashtags)

            }else {
                logger.warn("게시글 업데이트 실패. 권한이 없습니다. - {}", articleId)
                throw CustomException(ResultStatus.UNAUTHENTICATED_USER)
            }
        } catch (e: EntityNotFoundException) {
            logger.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.localizedMessage)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY, "게시글이 없습니다. - $articleId")
        }
    }

    /**
     * 게시글 삭제
     */
    fun deleteArticle(articleId: Long, memberId : String) {
        val article = articleRepository.getReferenceById(articleId)
        val hashtagIds = article.hashtags.mapNotNull { it.getId() }.toList()
        articleRepository.deleteArticle(articleId, memberId)
        articleRepository.flush()
        hashtagIds.forEach { hashtagService.deleteHashtagWithoutArticles(it) }
    }

    /**
     * 해시태그 갱신
     */
    fun renewHashtagsFromContent(content: String): List<Hashtag> {
        val hashtagNamesInContent = hashtagService.parseHashtagNames(content)
        val hashtags = hashtagService.findHashtagsByNames(hashtagNamesInContent).toMutableList()
        val existingHashtagNames = hashtags.map { it.hashtagName }.toSet()
        hashtagNamesInContent.filter { !existingHashtagNames.contains(it) }.forEach {
            hashtags.add(Hashtag(hashtagName = it))
        }
        return hashtags
    }
}