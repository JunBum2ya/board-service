package com.midas.boardservice

import com.midas.boardservice.config.TestAzureMailConfig
import com.midas.boardservice.security.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("testdb")
@TestPropertySource("classpath:application.yaml")
@Import(TestSecurityConfig::class, TestAzureMailConfig::class)
class BoardServiceApplicationTests {

    @Test
    fun contextLoads() {

    }

}
