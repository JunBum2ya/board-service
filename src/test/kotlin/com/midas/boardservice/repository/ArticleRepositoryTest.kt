package com.midas.boardservice.repository

import com.midas.boardservice.config.TestJpaConfig
import com.midas.boardservice.domain.Article
import com.midas.boardservice.domain.ArticleComment
import com.midas.boardservice.domain.Hashtag
import com.midas.boardservice.domain.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles

@DisplayName("Article 리포지토리 테스트")
@DataJpaTest
@ActiveProfiles("testdb")
@Import(TestJpaConfig::class)
class ArticleRepositoryTest(
    @Autowired private val articleRepository: ArticleRepository,
    @Autowired private val memberRepository: MemberRepository
) {

    private var article: Article? = null

    @BeforeEach
    fun initArticleData() {
        val member = memberRepository.save(Member(id = "test", email = "test@test.com", nickname = "test", password = "1234"))
        article = Article(member = member, title = "test", content = "content")
        article?.addHashtag(Hashtag(hashtagName = "test"))
        article?.let { articleRepository.save(it) }
    }

    @DisplayName("pageable로 검색을 한다면 article page가 반환된다.")
    @Test
    fun givenPageable_whenSearchArticle_thenReturnsArticlePage() {
        //given
        val pageable = Pageable.ofSize(10)
        //when
        val page = articleRepository.findAll(pageable)
        //then
        assertThat(page).isNotEmpty
        assertThat(page.size).isEqualTo(10)
        assertThat(page.first()).isEqualTo(article)
        assertThat(page.first().hashtags.size).isEqualTo(1)
    }

    @DisplayName("Member를 저장하면 member가 반환된다.")
    @Test
    fun givenCountAndMember_whenSaveMember_thenMemberCountIsMorePreviousCount() {
        //given
        val count = memberRepository.count()
        val member = memberRepository.save(Member(id = "member", email = "member@test.com", nickname = "test", password = "1234"))
        val article = Article(member = member, title = "test", content = "content")
        article.addHashtags(mutableSetOf(Hashtag(hashtagName = "save")))
        //when
        val savedArticle = articleRepository.save(article)
        //then
        assertThat(articleRepository.count()).isEqualTo(count + 1)
        assertThat(savedArticle).isNotNull
        assertThat(savedArticle).isEqualTo(article)
        assertThat(savedArticle.hashtags.size).isEqualTo(1)
    }

    @DisplayName("조회한 article을 수정하면 데이터가 수정된다.")
    @Test
    fun givenSearchedArticle_whenUpdateArticle_thenChangeArticle() {
        //given
        val article = articleRepository.findAll().first()
        //when
        article.update(title = "new-title", content = "new-content")
        article.clearHashtags()
        article.addHashtag(Hashtag(hashtagName = "spring"))
        //then
        val updatedArticle = articleRepository.findByIdOrNull(article.getId())
        assertThat(updatedArticle?.hashtags)
            .hasSize(1)
            .extracting("hashtagName",String::class.java)
            .containsExactly("spring")
    }

    @DisplayName("memberId로 삭제를 하면 DB에서 삭제된다.")
    @Test
    fun givenMemberId_whenDeleteMember_thenReduceMemberCount() {
        //given
        val articles = articleRepository.findAll()
        val count = articles.size
        val memberId = articles.first().getId()?:-1L
        //when
        articleRepository.deleteById(memberId)
        //then
        assertThat(articleRepository.count()).isEqualTo(count -1L)
    }
}