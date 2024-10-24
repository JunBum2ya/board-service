package com.midas.boardservice.controller

import com.midas.boardservice.domain.contant.FormStatus
import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.dto.request.ArticleRequest
import com.midas.boardservice.dto.response.ArticleResponse
import com.midas.boardservice.dto.response.ArticleWithCommentsResponse
import com.midas.boardservice.security.dto.BoardPrincipal
import com.midas.boardservice.service.ArticleService
import com.midas.boardservice.common.service.PaginationService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@RequestMapping("/articles")
@Controller
class ArticleController(private val articleService: ArticleService, private val paginationService: PaginationService) {

    /**
     * 게시글 조회 엔드포인트
     */
    @GetMapping
    fun articles(
        @RequestParam(required = false) searchType: SearchType? = null,
        @RequestParam(required = false) searchText: String? = null,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        map: ModelMap
    ): String {
        val articles = articleService.searchArticles(searchType, searchText, pageable).map { ArticleResponse.from(it) }
        val barNumbers = paginationService.getPaginationBarNumbers(articles.number, articles.totalPages)
        map["articles"] = articles
        map["paginationBarNumbers"] = barNumbers
        map["searchTypes"] = SearchType.entries.toTypedArray()
        map["searchTypeHashtag"] = SearchType.HASHTAG

        return "articles/index"
    }

    /**
     * 게시글 상세 조회 페이지
     */
    @GetMapping("/{articleId}")
    fun article(@PathVariable articleId: Long, map: ModelMap): String {
        val article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId))
        map["article"] = article
        map["articleComments"] = article.articleCommentsResponse
        map["totalCount"] = articleService.getArticleCount()
        map["searchTypeHashtag"] = SearchType.HASHTAG
        return "articles/detail"
    }

    /**
     * 게시글 등록 페이지
     */
    @GetMapping("/form")
    fun articleForm(map: ModelMap): String {
        map["formStatus"] = FormStatus.CREATE
        return "articles/form"
    }

    /**
     * 게시글 등록
     */
    @PostMapping("/form")
    fun postNewArticle(
        @AuthenticationPrincipal boardPrincipal: BoardPrincipal,
        @Valid articleRequest: ArticleRequest
    ): String {
        articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()))
        return "redirect:/articles"
    }

    /**
     * 게시글 수정 페이지
     */
    @GetMapping("/{articleId}/form")
    fun updateArticleForm(@PathVariable articleId: Long, map: ModelMap): String {
        val article = ArticleResponse.from(articleService.getArticle(articleId))
        map["article"] = article
        map["formStatus"] = FormStatus.UPDATE
        return "articles/form"
    }

    /**
     * 게시글 수정
     */
    @PostMapping("/{articleId}/form")
    fun updateArticle(
        @PathVariable articleId: Long,
        @AuthenticationPrincipal boardPrincipal: BoardPrincipal,
        articleRequest: ArticleRequest
    ): String {
        articleService.updateArticle(articleId, articleRequest.toDto(boardPrincipal.toDto()))
        return "redirect:/articles/${articleId}"
    }

    /**
     * 게시글 삭제
     */
    @PostMapping("/{articleId}/delete")
    fun deleteArticle(@PathVariable articleId: Long, @AuthenticationPrincipal boardPrincipal: BoardPrincipal): String {
        articleService.deleteArticle(articleId, boardPrincipal.username)
        return "redirect:/articles"
    }
}