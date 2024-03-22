package com.midas.boardservice.repository;

import com.midas.boardservice.domain.Hashtag
import org.springframework.data.jpa.repository.JpaRepository

interface HashtagRepository : JpaRepository<Hashtag, Long> {
}