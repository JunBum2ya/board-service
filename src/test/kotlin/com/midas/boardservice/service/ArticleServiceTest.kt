package com.midas.boardservice.service

import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.Hashtag
import com.midas.boardservice.domain.Member
import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.dto.ArticleDto
import com.midas.boardservice.dto.param.ArticleSearchParam
import com.midas.boardservice.repository.ArticleRepository
import com.midas.boardservice.repository.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class ArticleServiceTest : BehaviorSpec({

    val articleRepository = mockk<ArticleRepository>()
    val memberRepository = mockk<MemberRepository>()
    val hashtagService = mockk<HashtagService>()

    val articleService = ArticleService(articleRepository, memberRepository, hashtagService)

    Given("검색어 없이") {
        val pageable = Pageable.ofSize(20)
        every { articleRepository.findAll(any(Pageable::class)) }
            .returns(Page.empty())
        When("검색을 하면") {
            val page = articleService.searchArticles(null, null, pageable);
            Then("게시글 페이지를 반환한다.") {
                page.isEmpty shouldBe true
                verify { articleRepository.findAll(any(Pageable::class)) }
            }
        }
    }
    Given("검색어와 함께") {
        val searchType = SearchType.TITLE
        val searchText = "TITLE"
        val pageable = Pageable.ofSize(20)
        every { articleRepository.searchArticles(any(ArticleSearchParam::class), any(Pageable::class)) }
            .returns(Page.empty())
        When("게시글을 검색하면") {
            val page = articleService.searchArticles(searchType, searchText, pageable)
            Then("게시글 페이지를 반환한다.") {
                page.isEmpty shouldBe true
                verify { articleRepository.searchArticles(any(ArticleSearchParam::class),any(Pageable::class)) }
            }
        }
    }
    Given("존재하는 해시태그를 사용하여") {
        val hashtagNames = "kotlin spring jpa"
        val pageable = Pageable.ofSize(10)
        val expectedArticle = createArticle()
        every { articleRepository.searchArticles(any(ArticleSearchParam::class),any(Pageable::class)) }
            .returns(PageImpl<Article>(listOf(expectedArticle),pageable,1))
        When("해시태그 검색을 하면") {
            val page = articleService.searchArticles(SearchType.HASHTAG, hashtagNames, pageable)
            Then("게시글 페이지를 반환한다.") {
                page shouldNotBe null
                page.isEmpty shouldBe false
                page.totalElements shouldBe 1
                verify { articleRepository.searchArticles(any(ArticleSearchParam::class), any(Pageable::class)) }
            }
        }
    }
    Given("게시글 정보를") {
        val articleDto = ArticleDto.from(createArticle())
        val expectedHashtagNames = setOf("kotlin","spring")
        val expectedHashtags = arrayListOf(Hashtag(id = 1L, hashtagName = "kotlin"))
        every { memberRepository.getReferenceById(any(String::class)) }.returns(createMember())
        every { hashtagService.parseHashtagNames(any(String::class)) }.returns(expectedHashtagNames)
        every { hashtagService.findHashtagsByNames(any()) }.returns(expectedHashtags)
        every { articleRepository.save(any(Article::class)) }.returns(createArticle())
        When("입력하면") {
            val article = articleService.saveArticle(articleDto)
            Then("본문에서 해시태그 정보를 추출하여 해시태그 정보가 포함된 게시글을 생성한다") {
                article shouldNotBe null
                verify { memberRepository.getReferenceById(any(String::class)) }
                verify { hashtagService.parseHashtagNames(any(String::class)) }
                verify { hashtagService.findHashtagsByNames(any()) }
                verify { articleRepository.save(any(Article::class)) }
            }
        }
    }
}) {
    companion object {
        fun createArticle(): Article {
            return Article(id = 1L, member = createMember(), title = "title", content = "content")
        }

        fun createMember(): Member {
            return Member(id = "test", password = "test", nickname = "test", email = "test@test.com")
        }
    }
}