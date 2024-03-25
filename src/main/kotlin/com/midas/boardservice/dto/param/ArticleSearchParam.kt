package com.midas.boardservice.dto.param

import com.midas.boardservice.domain.Article
import com.querydsl.jpa.impl.JPAQuery

data class ArticleSearchParam(
    val title: String? = null,
    val content: String? = null,
    val memberId: String? = null,
    val nickname: String? = null,
    val hashtag: List<String>? = null
)