package com.midas.boardservice.article.repository;

import com.midas.boardservice.article.domain.Hashtag
import org.springframework.data.jpa.repository.JpaRepository

interface HashtagRepository : JpaRepository<Hashtag, Long> {
    fun findHashtagsByHashtagNameIn(hashtagNames: Collection<String>): List<Hashtag>
}