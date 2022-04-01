package ca.dal.cs.wanderer.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConf {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Wanderer")
                .description("Map based web-app developed by Group 21")
                .termsOfServiceUrl("open")
                .contact(new Contact("Group 21", "wanderer.com", "mg770477@dal.ca"))
                .version("Version 1")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                //.consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("ca.dal.cs.wanderer"))
                .paths(PathSelectors.any())
                //.paths(PathSelectors.ant("/book/*"))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Token", AUTHORIZATION_HEADER, "Header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Token", authorizationScopes));
    }
}
