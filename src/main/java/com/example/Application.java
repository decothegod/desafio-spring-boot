package com.example;

import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class, org.springdoc.core.converters.models.Pageable.class);
    }
}