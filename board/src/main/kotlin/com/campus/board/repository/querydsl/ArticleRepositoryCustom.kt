package com.campus.board.repository.querydsl

interface ArticleRepositoryCustom {
    fun findAllDistinctHashTags() : List<String>
}