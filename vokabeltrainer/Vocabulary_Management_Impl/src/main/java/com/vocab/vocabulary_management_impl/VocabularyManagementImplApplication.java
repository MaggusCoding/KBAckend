package com.vocab.vocabulary_management_impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.vocab.vocabulary_management_impl", "com.vocab.vocabulary_management"})
public class VocabularyManagementImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(VocabularyManagementImplApplication.class, args);
    }

}
