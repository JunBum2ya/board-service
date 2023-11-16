package com.campus.board.repository;

import com.campus.board.domain.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface ArticleRepository : JpaRepository<Article, Long> {
}