package com.midas.boardservice.service

import com.midas.boardservice.article.domain.Hashtag
import com.midas.boardservice.article.repository.HashtagRepository
import com.midas.boardservice.article.service.HashtagService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class HashtagServiceTest : BehaviorSpec({
    val hashtagRepository = mockk<HashtagRepository>()
    val hashtagService = HashtagService(hashtagRepository)

    Given("해시태그로 이루어진 평문이 입력되면") {
        When("해시태그를 추출한다.") {
            forAll(
                row(null, setOf()),
                row("", setOf()),
                row("   ", setOf()),
                row("#", setOf()),
                row("  #", setOf()),
                row("#java #spring  #부트", setOf("java", "spring", "부트"))
            ) { text: String?, result: Set<String> ->
                val actual = hashtagService.parseHashtagNames(text)
                actual shouldHaveSize result.size
                actual shouldContainExactly result
            }
        }
    }

    Given("해시태그 이름들을 입력하면") {
        val hashtagNames = listOf("spring", "java", "jpa")
        every { hashtagRepository.findHashtagsByHashtagNameIn(any()) }.returns(
            listOf(
                Hashtag(id = 1, hashtagName = "spring"),
                Hashtag(id = 2, hashtagName = "jpa")
            )
        )
        When("저장된 해시태그 중 이름에 매칭하는 것들을 조회하여") {
            val hashtags = hashtagService.findHashtagsByNames(hashtagNames.toSet())
            Then("중복 없이 반환한다.") {
                hashtags shouldHaveSize 2
                verify { hashtagRepository.findHashtagsByHashtagNameIn(any()) }
            }
        }
    }
}) {
}