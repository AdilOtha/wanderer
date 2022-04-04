package ca.dal.cs.wanderer.Config;

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

@EnableSwagger2
@Configuration
public class SwaggerConf {

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
                .select()
                .apis(RequestHandlerSelectors.basePackage("ca.dal.cs.wanderer"))
                .paths(PathSelectors.any())
                .build();
    }
}
