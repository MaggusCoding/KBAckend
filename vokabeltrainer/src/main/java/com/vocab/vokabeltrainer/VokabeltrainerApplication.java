package com.vocab.vokabeltrainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.vocab.vokabeltrainer","com.vocab.vocabulary_management_impl", "com.vocab.vocabulary_management"})
public class VokabeltrainerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VokabeltrainerApplication.class, args);
    }

}
