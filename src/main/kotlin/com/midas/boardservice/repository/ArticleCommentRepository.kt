package com.midas.boardservice.repository;

import com.midas.boardservice.domain.ArticleComment
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleCommentRepository : JpaRepository<ArticleComment, Long> {


    fun deleteByIdAndMember_Id(id: Long, id1: String): Long
}