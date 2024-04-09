package com.midas.boardservice.service

import com.midas.boardservice.domain.Hashtag
import com.midas.boardservice.domain.contant.ResultStatus
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.repository.HashtagRepository
import jakarta.persistence.EntityNotFoundException
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
     * 해시태그 파싱
     */
    fun parseHashtagNames(content: String?): Set<String> {
        if(content == null) {
            return setOf()
        }
        val pattern = Pattern.compile("#[\\w가-힣]+")
        val matcher = pattern.matcher(content)
        val result = mutableSetOf<String>()

        while(matcher.find()) {
            result.add(matcher.group().replace("#",""))
        }

        return result
    }

    /**
     * 게시글이 없는 해시태그 삭제
     */
    fun deleteHashtagWithoutArticles(hashtagId: Long) {
        try {
            val hashtag = hashtagRepository.getReferenceById(hashtagId)
            if(hashtag.articles.isEmpty()) {
                hashtagRepository.delete(hashtag)
            }
        }catch (e: EntityNotFoundException) {
            throw CustomException(ResultStatus.ACCESS_NOT_EXIST_ENTITY)
        }
    }
}