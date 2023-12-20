package com.vocab.user_management_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.user_management_impl.services.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepo userRepo;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    public void setup() {

        // Erstelle und füge einige Test-UserEntity-Objekte hinzu
        user1 = new UserEntity();
        user1.setUsername("testuser1");
        userRepo.save(user1);

        user2 = new UserEntity();
        user2.setUsername("testuser2");
        userRepo.save(user2);
    }

    @AfterEach
    public void tearDown(){
        userRepo.deleteAll(); // Setzt die Datenbank zurück
    }

    @Test
    public void testCreateUser() {
        // Erstelle einen neuen Benutzer
        UserEntity existingUser = userService.createUser("testuser1");
        UserEntity newUser = userService.createUser("testuser3");
        assertNotNull(newUser);
        assertEquals(existingUser.getUsername(), user1.getUsername() );
        assertEquals("testuser3", newUser.getUsername(), "Username stimmt nicht überein");
    }

    @Test
    public void testFindByUsername() {
        // Versuche, den Benutzer über findByUsername zu finden
        Optional<UserEntity> foundUser = userService.findByUsername("testuser1");

        // Überprüfe, ob der Benutzer gefunden wurde und ob die Daten übereinstimmen
        assertTrue(foundUser.isPresent(), "Benutzer wurde nicht gefunden");
        assertEquals("testuser1", foundUser.get().getUsername(), "Benutzername stimmt nicht überein");
    }

    @Test
    public void testDeleteUser() {
        // Lösche einen Benutzer
        userService.deleteUser(user1.getUserId());

        // Versuche, den Benutzer über findById zu finden
        Optional<UserEntity> foundUser = userService.findByUsername("testuser1");

        // Überprüfe, ob der Benutzer nicht gefunden wurde
        assertFalse(foundUser.isPresent(), "Benutzer wurde nicht gelöscht");
    }

    @Test
    public void testDeleteNotPersistedUser(){
        // Erstelle User-Objekt, aber es wird nicht persistiert
        UserEntity newUser = new UserEntity();
        newUser.setUserId(-1L);
        newUser.setUsername("notPersistedUser");

        // Führe Löschung durch
        boolean deleteSuccess = userService.deleteUser(newUser.getUserId());

        // Prüfe, dass nichts gelöscht wurde
        assertFalse(deleteSuccess, "Benutzer wurde nicht gelöscht");
    }

    @Test
    public void testGetById() {
        UserEntity foundUser = userService.getById(user1.getUserId());

        // Überprüfe, ob der abgerufene Benutzer korrekt ist
        assertNotNull(foundUser, "Benutzer wurde nicht gefunden");
        assertEquals(user1.getUsername(), foundUser.getUsername(), "Benutzernamen stimmen nicht überein");
    }

    @Test
    public void testGetAll() {
        // Füge hier mehrere Benutzer hinzu und überprüfe, ob alle abgerufen werden können
        UserEntity newUser = new UserEntity();
        newUser.setUsername("testuser3");
        // Setze hier weitere benötigte Felder
        userRepo.save(newUser);

        List<UserEntity> users = userService.getAll();

        // Überprüfe, ob alle Benutzer abgerufen wurden
        assertTrue(users.size() >= 2, "Nicht alle Benutzer wurden abgerufen");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser1")), "Benutzer 1 wurde nicht abgerufen");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser2")), "Benutzer 2 wurde nicht abgerufen");
    }
}