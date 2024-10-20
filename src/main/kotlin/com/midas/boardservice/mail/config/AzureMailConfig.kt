package com.midas.boardservice.mail.config

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.EmailClientBuilder
import com.azure.core.credential.AzureKeyCredential
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AzureMailConfig(
    @Value("\${mail.azure.app-name}") val appName: String,
    @Value("\${mail.azure.access-key}") val accessKey: String
) {

    @Bean
    fun emailAsyncClient(): EmailAsyncClient {
        val endpoint = "https://${appName}-communication.korea.communication.azure.com"
        val azureKeyCredential = AzureKeyCredential(accessKey)
        return EmailClientBuilder()
            .endpoint(endpoint)
            .credential(azureKeyCredential)
            .buildAsyncClient()
    }

}