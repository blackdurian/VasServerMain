package com.fyp.vasclinicserver.config;

import org.springframework.beans.factory.annotation.Value;
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
    @Value("${vas.cors.recipient.url}")
    private String recipientUrl;
    @Value("${vas.cors.clinic.url}")
    private String clinicUrl;
    @Value("${vas.cors.govtagency.url}")
    private String govtagencyUrl;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("VAS Clinic API")
                .version("1.0")
                .description(
                        String.format("API for VAS Clinic Server \nRecipient portal: %s \nClinic portaL: %s \nGovernment agencies portal: %s",
                        recipientUrl,clinicUrl,govtagencyUrl)
                )
                .contact(new Contact("Wei Quan Lo", "http://myFYP.com", "tp048381@mail.apu.edu.my"))
                .license("APU License of API")
                .build();
    }
}
