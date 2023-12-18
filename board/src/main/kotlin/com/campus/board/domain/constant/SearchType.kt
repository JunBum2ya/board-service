package com.campus.board.domain.constant

enum class SearchType(val korean: String) {
    NONE("검색"), TITLE(korean = "제목"), CONTENT(korean = "본문"), ID(korean = "아이디"), NICKNAME(korean = "닉네임"), HASHTAG(korean = "해시태그");
}