package com.midas.boardservice.service

import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.dto.ArticleCommentDto
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.repository.ArticleCommentRepository
import com.midas.boardservice.repository.ArticleRepository
import com.midas.boardservice.repository.MemberRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArticleCommentService(
    private val articleRepository: ArticleRepository,
    private val articleCommentRepository: ArticleCommentRepository,
    private val memberRepository: MemberRepository
) {

    private val logger = LoggerFactory.getLogger(ArticleCommentService::class.java)

    /**
     * 덧글 저장
     */
    @Throws(CustomException::class)
    fun saveArticleComment(articleCommentDto: ArticleCommentDto): ArticleCommentDto {
        try {
            val article = articleRepository.getReferenceById(articleCommentDto.articleId)
            val member = memberRepository.getReferenceById(articleCommentDto.member.id)
            val articleComment = articleCommentDto.toEntity(article = article, member = member)
            return articleCommentDto.parentCommentId?.let {
                val parentArticleComment = articleCommentRepository.getReferenceById(articleCommentDto.parentCommentId)
                parentArticleComment.addChildComment(articleComment)
                ArticleCommentDto.from(articleComment)
            }?: ArticleCommentDto.from(articleCommentRepository.save(articleComment))
        } catch (e: EntityNotFoundException) {
            logger.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.localizedMessage)
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }

    /**
     * 댓글 삭제
     */
    fun deleteArticleComment(commentId: Long, username: String) {
        articleCommentRepository.deleteByIdAndMember_Id(commentId, username)
    }
}