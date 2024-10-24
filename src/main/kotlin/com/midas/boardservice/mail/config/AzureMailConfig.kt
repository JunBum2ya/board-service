package com.midas.boardservice.mail.config

import com.azure.communication.email.EmailAsyncClient
import com.azure.communication.email.EmailClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.azure.core.http.HttpClient
import com.azure.core.http.HttpRequest
import com.azure.core.http.HttpResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration
class AzureMailConfig(
    @Value("\${mail.azure.app-name}") val appName: String,
    @Value("\${mail.azure.access-key}") val accessKey: String
) {

    @Bean
    fun emailAsyncClient(httpClient: HttpClient): EmailAsyncClient {
        val endpoint = "https://${appName}-communication.korea.communication.azure.com"
        val azureKeyCredential = AzureKeyCredential(accessKey)
        return EmailClientBuilder()
            .endpoint(endpoint)
            .credential(azureKeyCredential)
            .httpClient(httpClient)
            .buildAsyncClient()
    }

    @Bean
    fun httpClient(): HttpClient {
        return object : HttpClient {
            override fun send(request: HttpRequest): Mono<HttpResponse> {
                return Mono.empty()
            }
        }
    }

}