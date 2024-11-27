package com.midas.boardservice.config

import com.midas.boardservice.mail.config.AzureMailConfig
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

@TestConfiguration
@Import(AzureMailConfig::class)
class TestAzureMailConfig {
}