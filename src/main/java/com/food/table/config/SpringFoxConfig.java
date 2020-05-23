package com.food.table.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Value("${hostname}")
    String hostname;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(hostname)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey("accessToken", "Authorization", "header");
    }

}
