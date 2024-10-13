package com.midas.boardservice

import com.midas.boardservice.config.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("testdb")
@Import(TestSecurityConfig::class)
class BoardServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}
