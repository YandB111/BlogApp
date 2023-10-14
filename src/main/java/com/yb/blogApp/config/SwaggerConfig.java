package com.yb.blogApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {



    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yb.blogApp.controller"))
                .paths(PathSelectors.ant("/api/**")) // Adjust this to match your API paths
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("BlogApp API")
                .description("Blog Application API reference for developers")
                .termsOfServiceUrl("kashishkant428@gmail.com")
                .contact(new Contact("kashish", "https://github.com/YandB111", "kashishkant428@gmail.com"))
                .license("Yello and Black")
                .licenseUrl("contact-70090961715")
                .version("1.0")
                .build();
    }

}
