package com.vocab.application;

import com.vocab.application.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;

/**
 * TODO: 1. Try Catch Blöcke für alle Auswahlmöglichkeiten zwecks Exception Handling oder if Blöcke
 * 1. Javadoc überprüfen / Aufräumen Sachen die wir nicht brauchen
 * 2. Testfälle für alle Service Klassen neu machen mit Mockito siehe: https://github.com/MaggusCoding/webtechKassensystemBackend
 * 3. Kein Back Button in den Unterpunkten
 * 4. Einfach mal wenig rumzocken in der Anwendung
 * 5. Aufräumarbeiten(danach tests laufen lassen und 1x spielen):
 *  5.1. Überflüssige Tests entfernen: Tests in den API-Modulen, ApplicationTests, FlashcardImplTest, TranslationImplTest...
 *  5.2. überflüssige Services entfernen: FlashcardImpl, TranslationImpl...
 *  5.3. src-Folder auf root-Ebene
 *
 */
@Component
public class ConsoleApplication implements CommandLineRunner {

    @Autowired
    private MainController mainController;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        mainController.displayMainMenu(scanner);
    }
}




