package com.midas.boardservice.article.dto.param

data class ArticleSearchParam(
    val title: String? = null,
    val content: String? = null,
    val memberId: String? = null,
    val nickname: String? = null,
    val hashtag: List<String>? = null
)