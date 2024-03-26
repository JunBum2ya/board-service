package com.midas.boardservice.controller

import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.service.ArticleService
import com.midas.boardservice.service.PaginationService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@WebMvcTest
class ArticleControllerTest : DescribeSpec({
    val articleService = mockk<ArticleService>()
    val paginationService = mockk<PaginationService>()
    val articleController = ArticleController(articleService, paginationService)

    val mvc = MockMvcBuilders.standaloneSetup(articleController)
        .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
        .build()

    describe("[view][GET] 게시글 리스트 (게시판) 페이지") {
        every { articleService.searchArticles(any(), any(), any()) }.returns(Page.empty())
        every { paginationService.getPaginationBarNumbers(any(), any()) }.returns(listOf(0, 1, 2, 3, 4))
        it("정상호출") {
            mvc.get("/articles")
                .andExpect { status { isOk() } }
                .andExpect { model { attributeExists("articles") } }
                .andExpect { model { attributeExists("paginationBarNumbers") } }
                .andExpect { model { attributeExists("searchTypes") } }
                .andExpect { model { attribute("searchTypeHashtag", SearchType.HASHTAG) } }
                .andExpect { view { name("articles/index") } }
            verify { articleService.searchArticles(any(), any(), any()) }
            verify { paginationService.getPaginationBarNumbers(any(), any()) }
        }
        it("검색어와 함께 호출") {
            mvc.perform(
                get("/articles")
                    .queryParam("searchType", SearchType.TITLE.name)
                    .queryParam("searchText", "title")
            )
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchTypes"))
            verify { articleService.searchArticles(any(), any(), any()) }
            verify { paginationService.getPaginationBarNumbers(any(), any()) }
        }
        it("페이징, 정렬 기능") {
            val pageNumber = 3
            val pageSize = 20
            val sortName = "title"
            val direction = "desc"
            val pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)))

            mvc.perform(
                get("/articles")
                    .queryParam("page", pageNumber.toString())
                    .queryParam("size", pageSize.toString())
                    .queryParam("sort", "${sortName},${direction}")
            )
                .andExpect(status().isOk)
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"))
            verify { articleService.searchArticles(null, null, pageable) }
            verify { paginationService.getPaginationBarNumbers(any(),any()) }
        }
    }

})