package com.midas.boardservice.article.service

import com.midas.boardservice.article.domain.Hashtag
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.article.repository.HashtagRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern

@Transactional
@Service
class HashtagService(private val hashtagRepository: HashtagRepository) {
    /**
     * Hashtag 조회
     */
    @Transactional(readOnly = true)
    fun findHashtagsByNames(hashtagNames: Set<String>): List<Hashtag> {
        return hashtagRepository.findHashtagsByHashtagNameIn(hashtagNames)
    }

    /**
     * 게시글이 없는 해시태그 삭제
     */
    fun deleteHashtagWithoutArticles(hashtagId: Long) {
        val hashtag = hashtagRepository.findByIdOrNull(hashtagId)
            ?: throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        if(hashtag.articles.isEmpty()) {
            hashtagRepository.delete(hashtag)
        }
    }
}