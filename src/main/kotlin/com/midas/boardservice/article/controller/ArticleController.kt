package com.midas.boardservice.article.controller

import com.midas.boardservice.article.domain.constant.SearchType
import com.midas.boardservice.article.dto.request.ArticleRequest
import com.midas.boardservice.article.dto.response.ArticleResponse
import com.midas.boardservice.article.service.ArticleService
import com.midas.boardservice.common.dto.response.CommonResponse
import com.midas.boardservice.security.dto.BoardPrincipal
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/articles")
@RestController
class ArticleController(private val articleService: ArticleService) {

    /**
     * 게시글 조회 엔드포인트
     */
    @GetMapping
    fun articles(
        @RequestParam(required = false) searchType: SearchType? = null,
        @RequestParam(required = false) searchText: String? = null,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ResponseEntity<CommonResponse<Page<ArticleResponse>>> {
        val page = articleService.searchArticles(searchType, searchText, pageable).map { ArticleResponse.from(it) }
        return ResponseEntity.ok(CommonResponse.of(page))
    }

    /**
     * 게시글 추가 엔드포인트
     */
    @PostMapping
    fun postArticle(
        @Valid @RequestBody articleRequest: ArticleRequest,
        @AuthenticationPrincipal boardPrincipal: BoardPrincipal
    ): ResponseEntity<CommonResponse<ArticleResponse>> {
        val articleDto = articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()))
        return ResponseEntity.ok(CommonResponse.of(ArticleResponse.from(articleDto)))
    }

}