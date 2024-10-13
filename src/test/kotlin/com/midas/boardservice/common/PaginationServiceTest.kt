package com.midas.boardservice.common

import com.midas.boardservice.common.service.PaginationService
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class PaginationServiceTest : StringSpec({
    val paginationService = PaginationService()

    "현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다." {
        forAll(
            row(0, 13, listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
            row(1, 13, listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
            row(2, 13, listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
            row(3, 13, listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
            row(4, 13, listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
            row(5, 13, listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)),
            row(6, 13, listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
            row(10, 13, listOf(5, 6, 7, 8, 9, 10, 11, 12)),
            row(11, 13, listOf(6, 7, 8, 9, 10, 11, 12)),
            row(12, 13, listOf(7, 8, 9, 10, 11, 12))
        ) { currentPageNumber: Int, totalPages: Int, expected: List<Int> ->
            paginationService.getPaginationBarNumbers(currentPageNumber, totalPages) shouldBe expected
        }
    }
})