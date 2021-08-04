package com.bol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


/**
 * A <i>SwaggerConfig</i>. This class has responsibility to configure the swagger for exposing the apis<p>
 *
 * @author Omid Rostami
 */


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String GAME_CONTROLLER_TAG = "game controller";
    public static final String PLAYER_CONTROLLER_TAG = "player controller";


    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag(GAME_CONTROLLER_TAG, "Kalaha game API. Set of endpoints for Creating and Sowing the Game"),
                        new Tag(PLAYER_CONTROLLER_TAG, "Player game API. Set of endpoints for Creating and Updating the Players"))
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Kalaha",
                "This application provides api(s) to create and maintain services",
                "1.0",
                "all right reserved to bol.com",
                new Contact("Omid Rostami", "", "rostami.od@gmail.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList()
        );
    }

}
