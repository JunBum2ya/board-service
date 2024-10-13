package com.midas.boardservice.common.service

import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.stream.IntStream

@Service
class PaginationService {
    private val BAR_LENGTH = 10

    /**
     * 페이지 리스트 계산 후 반환
     */
    fun getPaginationBarNumbers(currentPageNumber: Int, totalPages: Int): List<Int> {
        val startNumber = (currentPageNumber - (BAR_LENGTH / 2)).coerceAtLeast(0)
        val endNumber = (startNumber + BAR_LENGTH).coerceAtMost(totalPages)
        return IntStream.range(startNumber,endNumber).boxed().collect(Collectors.toList())
    }
}