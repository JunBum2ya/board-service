package com.midas.boardservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.article.controller.ArticleController
import com.midas.boardservice.article.domain.Article
import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.domain.contant.FormStatus
import com.midas.boardservice.article.domain.constant.SearchType
import com.midas.boardservice.article.dto.ArticleDto
import com.midas.boardservice.article.dto.ArticleWithCommentsDto
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.article.dto.request.ArticleRequest
import com.midas.boardservice.article.service.ArticleService
import com.midas.boardservice.common.service.PaginationService
import com.midas.boardservice.util.FormDataEncoder
import com.midas.boardservice.util.TestAuthenticationPrincipal
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
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@WebMvcTest
class ArticleControllerTest : DescribeSpec({
    val articleService = mockk<ArticleService>()
    val paginationService = mockk<PaginationService>()
    val articleController = ArticleController(articleService, paginationService)
    val mapper = ObjectMapper()
    val formDataEncoder = FormDataEncoder(mapper)

    val mvc = MockMvcBuilders.standaloneSetup(articleController)
        .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver(),TestAuthenticationPrincipal())
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
            verify { paginationService.getPaginationBarNumbers(any(), any()) }
        }
    }

    describe("[view][GET] 새 게시글 작성 페이지") {
        it("정상 호출") {
            mvc.perform(get("/articles/form"))
                .andExpect(status().isOk)
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE))
        }
    }

    describe("[view][POST] 새 게시글 등록") {
        val request = ArticleRequest(title = "new title", content = "new content")
        every { articleService.saveArticle(any(ArticleDto::class)) }.returns(buildArticleDto())
        it("정상 호출") {
            mvc.perform(
                post("/articles/form")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(formDataEncoder.encode(request)!!)
            )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"))
            verify { articleService.saveArticle(any(ArticleDto::class)) }
        }
    }
    describe("[view][GET] 게시글 페이지") {
        val articleId = 1L
        val totalCount = 1L
        every { articleService.getArticleWithComments(any(Long::class)) }
            .returns(ArticleWithCommentsDto.from(buildArticle()))
        every { articleService.getArticleCount() }.returns(totalCount)
        it("정상 호출") {
            mvc.perform(get("/articles/${articleId}"))
                .andExpect(status().isOk)
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attribute("totalCount",totalCount))
                .andExpect(model().attribute("searchTypeHashtag", SearchType.HASHTAG))
            verify { articleService.getArticleWithComments(any(Long::class)) }
            verify { articleService.getArticleCount() }
        }
    }
    describe("[view][GET] 게시글 수정 페이지") {
        val articleId = 1L
        val article = buildArticleDto()
        every { articleService.getArticle(any(Long::class)) }.returns(article)
        it("정상 호출") {
            mvc.perform(get("/articles/${articleId}/form"))
                .andExpect(status().isOk)
                .andExpect(view().name("articles/form"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attribute("formStatus",FormStatus.UPDATE))
            verify { articleService.getArticle(any(Long::class)) }
        }
    }
    describe("[view][POST] 게시글 수정") {
        val articleId = 1L
        val articleRequest = ArticleRequest(title = "updated title", content = "updated content")
        every { articleService.updateArticle(any(Long::class), any(ArticleDto::class)) }.returns(Unit)
        it("정상 호출") {
            mvc.perform(post("/articles/${articleId}/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(articleRequest)!!)
            )
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/articles/${articleId}"))
                .andExpect(redirectedUrl("/articles/${articleId}"))
            verify { articleService.updateArticle(any(Long::class), any(ArticleDto::class)) }
        }
    }
}) {
    companion object {
        fun buildArticleDto(): ArticleDto {
            return ArticleDto.from(buildArticle())
        }

        fun buildMemberDto(): MemberDto {
            return MemberDto.from(buildMember())
        }

        private fun buildArticle(): Article {
            return Article(
                id = 1L,
                member = buildMember(),
                title = "title",
                content = "content"
            )
        }

        private fun buildMember(): Member {
            return Member(id = "test", email = "test@test.com", nickname = "test", password = "1234")
        }
    }
}