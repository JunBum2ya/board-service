package com.campus.board.controller

import com.campus.board.domain.constant.FormStatus
import com.campus.board.domain.constant.SearchType
import com.campus.board.dto.ArticleSearchParam
import com.campus.board.dto.request.ArticleRequest
import com.campus.board.dto.response.ArticleResponse
import com.campus.board.service.ArticleService
import com.campus.board.service.PaginationService
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/articles")
class ArticleController(val articleService: ArticleService,val paginationService: PaginationService) {

    @GetMapping
    fun articles(articleSearchParam: ArticleSearchParam,modelMap : ModelMap) : String {
        modelMap["searchTypes"] = SearchType.values()
        modelMap["articles"] = articleService.searchArticles(articleSearchParam)
        return "articles/index"
    }

    @GetMapping("/{articleId}")
    fun article(@PathVariable articleId : Long,map : ModelMap) : String {
        val articleResponse = ArticleResponse.from(articleService.getArticle(articleId))
        map["article"] = articleResponse
        return "articles/detail"
    }

    @GetMapping("/form")
    fun articleForm(map: ModelMap) : String {
        map["formStatus"] = FormStatus.CREATE
        return "articles/form"
    }

    @PostMapping("/form")
    fun postNewArticle(articleRequest : ArticleRequest) : String {
        articleService.createArticle(articleRequest.toDto())
        return "redirect:/articles"
    }

    @GetMapping("/{articleId}/form")
    fun updateArticleForm(@PathVariable articleId : Long,map : ModelMap) : String {
        val articleResponse = ArticleResponse.from(articleDto = articleService.getArticle(articleId))
        map["formStatus"] = FormStatus.UPDATE
        map["article"] = articleResponse
        return "articles/form"
    }

    @PostMapping("/{articleId}/form")
    fun updateArticle(@PathVariable articleId: Long,articleRequest: ArticleRequest) : String {
        articleService.updateArticle(articleId = articleId, articleDto = articleRequest.toDto())
        return "redirect:/articles/$articleId"
    }

    @PostMapping("/{articleId}/delete")
    fun deleteArticle(@PathVariable articleId: Long) : String {
        articleService.deleteArticle(articleId)
        return "redirect:/articles"
    }

}