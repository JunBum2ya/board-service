package com.campus.board.controller

import com.campus.board.domain.constant.FormStatus
import com.campus.board.domain.constant.SearchType
import com.campus.board.dto.ArticleSearchParam
import com.campus.board.dto.request.ArticleRequest
import com.campus.board.dto.response.ArticleResponse
import com.campus.board.service.ArticleService
import com.campus.board.service.PaginationService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/articles")
class ArticleController(val articleService: ArticleService, val paginationService: PaginationService) {

    @GetMapping
    fun articles(
        @RequestParam(required = false) searchType: SearchType?,
        @RequestParam(required = false) searchValue: String?,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        map: ModelMap
    ): String {
        val articles = articleService.searchArticles(
            searchValue = searchValue,
            searchType = searchType?:SearchType.NONE,
            pageable = pageable
        ).map { ArticleResponse.from(it) }
        val barNumbers = paginationService.getPaginationBarNumbers(currentPageNumber = pageable.pageNumber, totalPages =  articles.totalPages)
        map["articles"] = articles
        map["paginationBarNumbers"] = barNumbers
        map["searchTypes"] = SearchType.values()
        return "articles/index"
    }

    @GetMapping("/{articleId}")
    fun article(@PathVariable articleId: Long, map: ModelMap): String {
        val articleResponse = ArticleResponse.from(articleService.getArticle(articleId))
        map["article"] = articleResponse
        return "articles/detail"
    }

    @GetMapping("/form")
    fun articleForm(map: ModelMap): String {
        map["formStatus"] = FormStatus.CREATE
        return "articles/form"
    }

    @PostMapping("/form")
    fun postNewArticle(articleRequest: ArticleRequest): String {
        articleService.createArticle(articleRequest.toDto())
        return "redirect:/articles"
    }

    @GetMapping("/{articleId}/form")
    fun updateArticleForm(@PathVariable articleId: Long, map: ModelMap): String {
        val articleResponse = ArticleResponse.from(articleDto = articleService.getArticle(articleId))
        map["formStatus"] = FormStatus.UPDATE
        map["article"] = articleResponse
        return "articles/form"
    }

    @PostMapping("/{articleId}/form")
    fun updateArticle(@PathVariable articleId: Long, articleRequest: ArticleRequest): String {
        articleService.updateArticle(articleId = articleId, articleDto = articleRequest.toDto())
        return "redirect:/articles/$articleId"
    }

    @PostMapping("/{articleId}/delete")
    fun deleteArticle(@PathVariable articleId: Long): String {
        articleService.deleteArticle(articleId)
        return "redirect:/articles"
    }

}