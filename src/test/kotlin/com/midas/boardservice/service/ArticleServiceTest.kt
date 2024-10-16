package com.midas.boardservice.service

import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.Hashtag
import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.common.domain.constant.ResultStatus
import com.midas.boardservice.domain.contant.SearchType
import com.midas.boardservice.dto.ArticleDto
import com.midas.boardservice.member.dto.MemberDto
import com.midas.boardservice.dto.param.ArticleSearchParam
import com.midas.boardservice.exception.CustomException
import com.midas.boardservice.repository.ArticleRepository
import com.midas.boardservice.member.repository.MemberRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

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
    Given("게시글 ID로 조회를 하면") {
        val articleId = 1L
        val article = createArticle()
        When("게시글이 있을 경우") {
            every { articleRepository.findByIdOrNull(any(Long::class)) }.returns(article)
            val articleWithComments = articleService.getArticleWithComments(articleId)
            Then("댓글 달린 게시글을 반환한다.") {
                articleWithComments shouldNotBe null
                articleWithComments.title shouldBe article.getTitle()
                articleWithComments.content shouldBe article.getContent()
                verify { articleRepository.findByIdOrNull(any(Long::class)) }
            }
        }
        When("게시글이 없을 경우") {
            every { articleRepository.findByIdOrNull(any(Long::class)) }.returns(null)
            val customException = shouldThrow<CustomException> { articleService.getArticleWithComments(articleId) }
            Then("예외가 발생한다.") {
                customException.code shouldBe ResultStatus.ACCESS_NOT_EXIST_ENTITY.code
                customException.message shouldBe "게시글이 없습니다. - 1"
            }
        }
    }
    Given("게시글 수를") {
        val expected = 0L
        every { articleRepository.count() }.returns(expected)
        When("조회하면") {
            val actual = articleService.getArticleCount()
            Then("게시글 수를 반환한다.") {
                actual shouldBe expected
                verify { articleRepository.count() }
            }
        }
    }
    Given("게시글 수정 정보를 입력했을 때") {
        val article = createArticle()
        val articleDto = ArticleDto(memberDto = MemberDto.from(article.member), title = "new title", content = "new content #spring")
        val hashtagNames = hashSetOf("spring")
        every { articleRepository.getReferenceById(any(Long::class)) }.returns(article)
        every { memberRepository.getReferenceById(any(String::class)) }.returns(createMember())
        every { hashtagService.deleteHashtagWithoutArticles(any(Long::class)) }.returns(Unit)
        every { hashtagService.parseHashtagNames(any(String::class)) }.returns(hashtagNames)
        every { hashtagService.findHashtagsByNames(any()) }.returns(listOf<Hashtag>())
        every { articleRepository.flush() }.returns(Unit)
        When("존재하는 게시글이었을 경우") {
            articleService.updateArticle(articleDto.articleId?:-1L, articleDto)
            Then("게시글을 수정한다.") {
                article.getTitle() shouldBe articleDto.title
                article.getContent() shouldBe articleDto.content
                verify { articleRepository.getReferenceById(any(Long::class)) }
                verify { memberRepository.getReferenceById(any(String::class)) }
                verify { hashtagService.deleteHashtagWithoutArticles(any(Long::class)) }
                verify { hashtagService.parseHashtagNames(any(String::class)) }
                verify { hashtagService.findHashtagsByNames(any()) }
                verify { articleRepository.flush() }
            }
        }
    }
}) {
    companion object {
        fun createArticle(): Article {
            val article =  Article(id = 1L, member = createMember(), title = "title", content = "content")
            article.addHashtag(Hashtag(id = 1L, hashtagName = "kotlin"))
            article.addHashtag(Hashtag(id = 2L, hashtagName = "spring"))
            return article
        }

        fun createMember(): Member {
            return Member(id = "test", password = "test", nickname = "test", email = "test@test.com")
        }
    }
}