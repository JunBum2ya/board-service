package com.campus.board.domain

import java.time.LocalDateTime

/**
 * id : 아이디
 * title : 제목
 * hashTag : 해시태그
 * content 본문
 */
class Article(var id : Long,var title : String,var hashTag : String,var content : String) {
    var createdAt : LocalDateTime = LocalDateTime.now() //생성일
    var createdBy : String = "" //생성자
    var modifiedAt : LocalDateTime? = null //수정일
    var modifiedBy : String? = "" //수정자
}