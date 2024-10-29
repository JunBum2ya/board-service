package com.midas.boardservice.article.repository

import com.midas.boardservice.config.TestJpaConfig
import com.midas.boardservice.article.domain.Article
import com.midas.boardservice.article.domain.ArticleComment
import com.midas.boardservice.article.domain.Hashtag
import com.midas.boardservice.member.domain.Member
import com.midas.boardservice.article.dto.param.ArticleSearchParam
import com.midas.boardservice.member.repository.MemberRepository
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
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val articleCommentRepository: ArticleCommentRepository
) {
    private var article: Article? = null

    @BeforeEach
    fun initArticleData() {
        val member =
            memberRepository.save(Member(id = "test", email = "test@test.com", nickname = "test", password = "1234"))
        article = Article(member = member, title = "test", content = "content")
        article?.addHashtag(Hashtag(hashtagName = "test"))
        article?.let {
            val articleComment = ArticleComment(article = it, member = member, content = "test")
            it.articleComments.add(articleComment)
            articleRepository.saveAndFlush(it)
            articleComment.addChildComment(ArticleComment(article = it, member = member, content = "test1"))
            articleComment.addChildComment(ArticleComment(article = it, member = member, content = "test2"))
        }
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

    @DisplayName("빈 파라미터와 pageable로 검색을 한다면 article page가 반환된다.")
    @Test
    fun givenEmptyArticleSearchParamAndPageable_whenSearchArticle_thenReturnsArticlePage() {
        //given
        initQueryDslTestData()
        val param = ArticleSearchParam()
        val pageable = Pageable.ofSize(10)
        //when
        val page = articleRepository.searchArticles(param,pageable)
        //then
        assertThat(page).isNotEmpty
        assertThat(page.size).isEqualTo(10)
        assertThat(page.first().hashtags.size).isEqualTo(1)
    }

    @DisplayName("파라미터와 pageable로 검색을 한다면 article page가 반환된다.")
    @Test
    fun givenArticleSearchParamAndPageable_whenSearchArticle_thenReturnsArticlePage() {
        //given
        initQueryDslTestData()
        val param = ArticleSearchParam(title = "one", hashtag = listOf("과학","영어"))
        val pageable = Pageable.ofSize(10)
        //when
        val page = articleRepository.searchArticles(param,pageable)
        //then
        //assertThat(page).isNotEmpty
        //assertThat(page.size).isEqualTo(10)
        //assertThat(page.first().hashtags.size).isEqualTo(2)
    }

    @DisplayName("파라미터와 pageable로 검색을 한다면 article page가 반환된다.")
    @Test
    fun givenHashtagNamesAndPageable_whenSearchArticle_thenReturnsArticlePage() {
        //given
        initQueryDslTestData()
        val hashtagNames = listOf("영어", "과학")
        val pageable = Pageable.ofSize(10)
        //when
        val page = articleRepository.findByHashtagNames(hashtagNames,pageable);
        //then
        assertThat(page).isNotEmpty
        assertThat(page.size).isEqualTo(10)
        assertThat(page.first().hashtags.size).isEqualTo(2)
    }

    @DisplayName("Member를 저장하면 member가 반환된다.")
    @Test
    fun givenCountAndMember_whenSaveMember_thenMemberCountIsMorePreviousCount() {
        //given
        val count = memberRepository.count()
        val member = memberRepository.save(
            Member(
                id = "member",
                email = "member@test.com",
                nickname = "test",
                password = "1234"
            )
        )
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
            .extracting("hashtagName", String::class.java)
            .containsExactly("spring")
    }

    @DisplayName("memberId로 삭제를 하면 DB에서 삭제된다.")
    @Test
    fun givenMemberId_whenDeleteMember_thenReduceMemberCount() {
        //given
        val articles = articleRepository.findAll()
        val count = articles.size
        val memberId = articles.first().getId() ?: -1L
        //when
        articleRepository.deleteById(memberId)
        //then
        assertThat(articleRepository.count()).isEqualTo(count - 1L)
    }

    @DisplayName("댓글 아이디로 조회를 하면 댓글이 반환된다.")
    @Test
    fun givenArticleCommentId_whenSearchArticleComment_thenReturnsArticleComment() {
        //given
        val articleCommentId = article?.articleComments?.first()?.getId() ?: -1L
        //when
        val articleComment = articleCommentRepository.findByIdOrNull(articleCommentId)
        //then
        assertThat(articleComment)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", articleCommentId)
    }

    @DisplayName("댓글에 대댓글을 저장한다.")
    @Test
    fun givenParentComment_whenSaveArticleComment_thenInsertsChildComment() {
        //given
        val articleComment = article!!.articleComments.first()
        //when
        articleComment.addChildComment(
            ArticleComment(
                member = articleComment.member,
                article = articleComment.article,
                content = "test"
            )
        )
        articleCommentRepository.flush()
        //then
        val updatedComment = articleCommentRepository.findByIdOrNull(articleComment.getId())!!
        assertThat(updatedComment).hasFieldOrPropertyWithValue("parentCommentId",null)
        assertThat(updatedComment.childComments).hasSize(1)
    }

    fun initQueryDslTestData() {
        val hashtag1 = Hashtag(hashtagName = "과학")
        val hashtag2 = Hashtag(hashtagName = "사회")
        val member = memberRepository.save(Member(id = "query", email = "query@test.com", nickname = "query", password = "1234"))
        val article1 = Article(member = member, title = "one", content = "first")
        article1.addHashtags(listOf(hashtag1,hashtag2))
        val article2 = Article(member = member, title = "two", content = "first_second")
        article2.addHashtag(hashtag1)
        val article3 = Article(member = member, title = "three", content = "third")
        article3.addHashtag(hashtag2)
        articleRepository.save(article1)
        articleRepository.save(article2)
        articleRepository.save(article3)
    }
}