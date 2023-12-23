package com.vocab.application_ui_impl.views;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class DbView {

    private final Scanner scanner = new Scanner(System.in);

    public void displayOptions() {
        System.out.println("This will clear the in-memory database completely! Are you sure? (y/n)");
    }

    public String readString() {
        return scanner.next();
    }

    public void printSuccessMessage() {
        System.out.println("In-memory database cleared successfully.");
    }

    public void printFailMessage() {
        System.out.println("Database clear cancelled.");
    }
}
