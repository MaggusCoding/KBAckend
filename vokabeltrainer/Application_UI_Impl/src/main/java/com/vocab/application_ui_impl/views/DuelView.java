package com.vocab.application_ui_impl.views;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class DuelView {

    private final Scanner scanner = new Scanner(System.in);

    public Long readLong() {
        return scanner.nextLong();
    }

    public int readInt() {
        return scanner.nextInt();
    }

    public String readString(){
        return scanner.next();
    }

    public void printAvailableFlashcardLists(String msg) {
        System.out.println(msg);
    }

    public void printCreateDuelInstruction() {
        System.out.print("Enter the ID of the Flashcard List for the duel or '0' to go back: ");
    }

    public void printInputFailMessage() {
        System.out.println("Entered ID is invalid. Try again!");
    }

    public void printDuelCreated(Long duelId) {
        System.out.println("Duel created with ID: " + duelId);
    }

    public void printAvailableDuelsToJoin(String msg) {
        System.out.println(msg);
    }

    public void printJoinInstruction() {
        System.out.print("Enter the ID of the Duel to Join or '0' to go back: ");
    }

    public void printSuccessfulJoinedDuel() {
        System.out.println("Successfully joined Duel");
    }

    public void printFailedJoiningDuel() {
        System.out.println("Error joining Duel. It might be already full or started.");
    }

    public void printAvailableDuelsToStart(String msg) {
        System.out.println(msg);
    }

    public void printStartInstruction() {
        System.out.println("Enter the ID of the Duel to start or '0' to go back:");
    }

    public void printCurrentRound(int x) {
        System.out.println("#################### round " + x + "/10 ####################");
    }

    public void printEndOfRound() {
        System.out.println("Let´s begin the next round!");
    }

    public void printEndOfDuel() {
        System.out.println("All rounds were played. Let´s see who wins! ");
    }

    public void printWinners(String msg) {
        System.out.println(msg);
    }

    public void printNoJoinableDuel() {
        System.out.println("Apparently you didn´t join a duel.");
    }

    public void printAvailableDuelsToDelete(String msg) {
        System.out.println(msg);
    }

    public void printDeleteInstruction() {
        System.out.print("Enter the ID of the Duel to delete or '0' to go back: ");
    }

    public void printErrorMessage(String errorMsg) {
        System.out.println(errorMsg);
    }

    public void printDuelDeleted() {
        System.out.println("Duel deleted successfully.");
    }

    public void printCurrentPlayer(String username) {
        System.out.println("#################### You are now " +  username + " ####################");
    }

    public void printQuestion(String question) {
        System.out.println("What is the translation for: " + question + "? (Enter the id of the answer.)");
    }

    public void printAnswer(String answer) {
        System.out.println(answer);
    }

    public void printAllAnswered() {
        System.out.println("All players played this round.");
    }

    public void printUserNotExist() {
        System.out.println("Typed username does not exist. Choose other player!");
    }

    public void printUserNotExist2(String message) {
        System.out.println(message);
    }

    public void printUserAlreadyPlayed() {
        System.out.println("User already played.");
    }

    public void printUserAlreadyPlayed2(String message) {
        System.out.println(message);
    }

    public void printUserNotParticipating() {
        System.out.println("This user is not participating in the current duel! Choose other player!");
    }

    public void printUserNotParticipating2(String message) {
        System.out.println(message);
    }

    public void printNextPlayer() {
        System.out.println("Which user should answer next? (Enter username or type exit to leave)");
    }

    public void printAvailableNextPlayer(String users) {
        System.out.println(users);
    }

    public void printDuelNotExists(String message) {
        System.out.println(message);
    }

    public void printDuelAlreadyStarted(String message) {
        System.out.println(message);
    }

    public void printUserAlreadyPartOfDuel(String message) {
        System.out.println(message);
    }

    public void printFlashcardListNotExists(String message) {
        System.out.println(message);
    }
}
