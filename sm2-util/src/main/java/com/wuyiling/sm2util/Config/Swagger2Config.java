package com.wuyiling.sm2util.Config;

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
public class Swagger2Config {

    @Bean
    public Docket docket(){
        return  new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)              //是否激活swagger,如果是正式环境下，请把他设置为false
                .groupName("赵成默")          //分组，可以设置多个docket
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zzs.swagger.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("赵成默")
                .description("swagger学习")
                .version("1.0")
                .termsOfServiceUrl("termsOfServiceUrl")
                .contact(new Contact("name","url","email"))
                .build();
    }

}