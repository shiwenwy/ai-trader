package com.dodo.ai_trader.web.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: shiwen
 * Date: 2025/1/15 15:56
 * Description:
 */
@OpenAPIDefinition(
        info = @Info(
                title = "系统 API 文档",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("系统 API")
                .pathsToMatch("/**")
                .build();
    }
}
