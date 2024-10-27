package com.midas.boardservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.boardservice.article.controller.ArticleCommentController
import com.midas.boardservice.article.dto.ArticleCommentDto
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.article.service.ArticleCommentService
import com.midas.boardservice.util.FormDataEncoder
import com.midas.boardservice.util.TestAuthenticationPrincipal
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class ArticleCommentControllerTest: DescribeSpec({
    val articleCommentService = mockk<ArticleCommentService>()
    val articleCommentController = ArticleCommentController(articleCommentService)
    val mapper = ObjectMapper()
    val formDataEncoder = FormDataEncoder(mapper)

    val mvc = MockMvcBuilders
        .standaloneSetup(articleCommentController)
        .setCustomArgumentResolvers(TestAuthenticationPrincipal())
        .build()

    describe("[view][POST] 댓글 등록") {
        val request = HashMap<String,String>()
        request["articleId"] = "1"
        request["content"] = "test"
        val member = MemberDto(id = "test", password = "1234", email = "test@test.com", nickname = "test")
        every { articleCommentService.saveArticleComment(any(ArticleCommentDto::class)) }
            .returns(ArticleCommentDto(articleId = 1L,content = "test", member = member))
        it("정상 호출") {
            mvc.perform(post("/comments/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(request)!!))
                .andExpect(status().is3xxRedirection)
                .andExpect(view().name("redirect:/articles/1"))
                .andExpect(redirectedUrl("/articles/1"))
            verify { articleCommentService.saveArticleComment(any(ArticleCommentDto::class)) }
        }
    }

}){

}