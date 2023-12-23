package com.vocab.application;

import com.vocab.application_ui_api.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleApplication implements CommandLineRunner {

    @Autowired
    private MainController mainController;

    @Override
    public void run(String... args) {
        mainController.displayMainMenu();
    }
}




