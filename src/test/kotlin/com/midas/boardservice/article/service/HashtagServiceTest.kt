package com.midas.boardservice.article.service

import com.midas.boardservice.article.domain.Hashtag
import com.midas.boardservice.article.repository.HashtagRepository
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