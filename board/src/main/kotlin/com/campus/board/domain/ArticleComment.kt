package com.campus.board.domain

import java.time.LocalDateTime

/**
 * 댓글 도메인
 * id : 아이디
 * article : 게시글
 * content : 본문
 */
class ArticleComment(var id : Long,var article : Article,var content : String) {
    var createdAt : LocalDateTime = LocalDateTime.now() //생성일
    var createdBy : String = "" //생성자
    var modifiedAt : LocalDateTime? = null //수정일
    var modifiedBy : String? = "" //수정자
}