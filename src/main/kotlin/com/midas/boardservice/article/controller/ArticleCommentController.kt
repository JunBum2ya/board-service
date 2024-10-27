package com.midas.boardservice.article.controller

import com.midas.boardservice.article.dto.request.ArticleCommentRequest
import com.midas.boardservice.security.dto.BoardPrincipal
import com.midas.boardservice.article.service.ArticleCommentService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/comments")
@Controller
class ArticleCommentController(private val articleCommentService: ArticleCommentService) {

    @PostMapping("/new")
    fun postNewArticleComment(
        @AuthenticationPrincipal boardPrincipal: BoardPrincipal,
        @Valid request: ArticleCommentRequest
    ): String {
        articleCommentService.saveArticleComment(request.toDto(boardPrincipal.toDto()))
        return "redirect:/articles/${request.articleId}"
    }

    @PostMapping("/{commentId}/delete")
    fun deleteArticleComment(
        @PathVariable commentId: Long,
        principal: BoardPrincipal,
        @NotNull(message = "게시글 아이디를 입력하세요.") articleId: Long?
    ): String {
        articleCommentService.deleteArticleComment(commentId, principal.username)
        return "redirect:/articles/$commentId"
    }

}