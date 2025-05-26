package com.kvote.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI basicOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KVote API")
                        .version("v1")
                        .description("KVote 백엔드 API 문서"));
    }
}
