package com.midas.boardservice.article.domain.constant

enum class SearchType(val korean: String) {
    TITLE("제목"), CONTENT("본문"), ID("유저 ID"), NICKNAME("닉네임"), HASHTAG("해시태그")
}