package com.campus.board.dto

import com.campus.board.domain.constant.SearchType
import org.springframework.data.domain.Pageable

class ArticleSearchParam(
    val searchType: SearchType = SearchType.NONE,
    val searchKeyword: String? = null
)