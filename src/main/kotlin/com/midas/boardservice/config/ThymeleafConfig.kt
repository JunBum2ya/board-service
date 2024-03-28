package com.midas.boardservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver

/**
 * Thymeleaf decoupled logic 적용
 */
@Configuration
class ThymeleafConfig {
    @Bean
    fun thymeleafTemplateResolver(
        defaultTemplateResolver: SpringResourceTemplateResolver,
        thymeleaf3Properties: Thymeleaf3Properties
    ): SpringResourceTemplateResolver {
        defaultTemplateResolver.useDecoupledLogic = thymeleaf3Properties.decoupledLogic
        return defaultTemplateResolver
    }
}

@ConfigurationProperties(value = "spring.thymeleaf3")
class Thymeleaf3Properties(val decoupledLogic: Boolean = false)