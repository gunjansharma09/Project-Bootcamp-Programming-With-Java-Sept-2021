package com.bootcampproject.bootcamp_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.GET;


import java.net.URI;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing //  created date ,last modified ,generate krne k lie use hotih ..
public class BootcampProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootcampProjectApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/"), req ->
                ServerResponse.temporaryRedirect(URI.create("swagger-ui.html")).build());
    }

}
