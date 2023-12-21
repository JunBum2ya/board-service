package com.campus.board.service

import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.stream.IntStream

@Service
class PaginationService {
    val BAR_LENGTH = 5

    fun getPaginationBarNumbers(currentPageNumber: Int, totalPages: Int) : List<Int> {
        val startNumber = (currentPageNumber - (BAR_LENGTH / 2)).coerceAtLeast(0)
        val endNumber = (startNumber + BAR_LENGTH).coerceAtMost(totalPages)
        return IntStream.range(startNumber,endNumber).boxed().collect(Collectors.toList())
    }

}