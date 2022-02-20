package ca.dal.cs.wanderer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwagger2
@Configuration
public class SwaggerConf {
private ApiInfo apiInfo(){
    return new ApiInfoBuilder()
            .title("Wanderer")
            .description("Dev team is Group 21 abd client team is Group 6")
            .termsOfServiceUrl("open")
            .contact(new Contact("Group 21","need to mention our email ids here"))
            .build();
}

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
