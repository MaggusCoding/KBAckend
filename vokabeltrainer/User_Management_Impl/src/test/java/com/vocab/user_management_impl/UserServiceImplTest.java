package com.vocab.user_management_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management.repos.UserRepo;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel_API.repositories.DuelRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private DuelRepo duelRepo;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    public void setup() {

        // Erstelle und füge einige Test-UserEntity-Objekte hinzu
        user1 = new UserEntity();
        user1.setUserId(1L);
        user1.setUsername("testuser1");

        user2 = new UserEntity();
        user2.setUserId(2L);
        user2.setUsername("testuser2");

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        UserEntity user3 = new UserEntity();
        user3.setUserId(3L);
        user3.setUsername("testuser3");
        when(userRepo.findByUsername("testuser1")).thenReturn(Optional.ofNullable(user1));
        when(userRepo.findByUsername(user3.getUsername())).thenReturn(Optional.empty());
        when(userRepo.save(any(UserEntity.class))).thenReturn(user3);

        // Erstelle einen neuen Benutzer
        UserEntity existingUser = userService.createUser("testuser1");
        UserEntity newUser = userService.createUser("testuser3");

        assertNotNull(newUser);
        assertEquals(existingUser.getUserId(), user1.getUserId() );
        assertEquals(existingUser.getUsername(), user1.getUsername() );
        assertEquals(user3.getUserId(), newUser.getUserId(), "neuer User wurde nicht angelegt");
        assertEquals(user3.getUsername(), newUser.getUsername(), "Username stimmt nicht überein");
        verify(userRepo, times(2)).findByUsername(anyString());
        verify(userRepo, times(0)).save(user1);
        verify(userRepo).save(any(UserEntity.class));
    }

    @Test
    public void testFindByUsername() {
        when(userRepo.findByUsername(user1.getUsername())).thenReturn(Optional.ofNullable(user1));

        // Versuche, den Benutzer über findByUsername zu finden
        UserEntity foundUser = userService.findByUsername(user1.getUsername());

        // Überprüfe, ob der Benutzer gefunden wurde und ob die Daten übereinstimmen
        assertNotNull(foundUser, "Benutzer wurde nicht gefunden");
        assertEquals("testuser1", foundUser.getUsername(), "Benutzername stimmt nicht überein");
        verify(userRepo).findByUsername(user1.getUsername());
    }

    @Test
    public void testDeleteUser() {
        when(userRepo.existsById(user1.getUserId())).thenReturn(true);
        when(userRepo.findById(user1.getUserId())).thenReturn(Optional.of(user1));
        when(duelRepo.existsDuelByPlayersIsContaining(user1)).thenReturn(false);

        boolean deleteSuccess = userService.deleteUser(user1.getUserId());

        assertTrue(deleteSuccess);
        verify(userRepo).existsById(user1.getUserId());
        verify(userRepo).findById(user1.getUserId());
        verify(duelRepo).existsDuelByPlayersIsContaining(user1);
        verify(userRepo).deleteById(user1.getUserId());
    }

    @Test
    public void testDeleteNotPersistedUser(){
        // Erstelle User-Objekt, aber es wird nicht persistiert
        UserEntity newUser = new UserEntity();
        newUser.setUserId(3L);
        newUser.setUsername("notPersistedUser");
        when(userRepo.existsById(newUser.getUserId())).thenReturn(false);
        when(userRepo.findById(newUser.getUserId())).thenReturn(Optional.of(newUser));
        when(duelRepo.existsDuelByPlayersIsContaining(newUser)).thenReturn(false);

        // Führe Löschung durch
        boolean deleteSuccess = userService.deleteUser(newUser.getUserId());

        // Prüfe, dass nichts gelöscht wurde
        assertFalse(deleteSuccess, "Benutzer wurde gelöscht");
        verify(userRepo).existsById(newUser.getUserId());
        verify(userRepo, times(0)).findById(newUser.getUserId());
        verify(duelRepo, times(0)).existsDuelByPlayersIsContaining(newUser);
        verify(userRepo, times(0)).deleteById(newUser.getUserId());
    }

    @Test
    public void testDeleteUserWhoPlaysInDuel(){
        // Erstelle User-Objekt, aber es wird nicht persistiert
        UserEntity newUser = new UserEntity();
        newUser.setUserId(3L);
        newUser.setUsername("player");
        when(userRepo.existsById(newUser.getUserId())).thenReturn(true);
        when(userRepo.findById(newUser.getUserId())).thenReturn(Optional.of(newUser));
        when(duelRepo.existsDuelByPlayersIsContaining(newUser)).thenReturn(true);

        // Führe Löschung durch
        boolean deleteSuccess = userService.deleteUser(newUser.getUserId());

        // Prüfe, dass nichts gelöscht wurde
        assertFalse(deleteSuccess, "Benutzer wurde gelöscht");
        verify(userRepo, times(0)).deleteById(newUser.getUserId());
        verify(duelRepo, times(1)).existsDuelByPlayersIsContaining(newUser);
        verify(userRepo, times(1)).existsById(newUser.getUserId());
    }

    @Test
    public void testGetById() {
        when(userRepo.findById(user1.getUserId())).thenReturn(Optional.ofNullable(user1));

        UserEntity foundUser = userService.getById(user1.getUserId());

        // Überprüfe, ob der abgerufene Benutzer korrekt ist
        assertNotNull(foundUser, "Benutzer wurde nicht gefunden");
        assertEquals(user1.getUsername(), foundUser.getUsername(), "Benutzernamen stimmen nicht überein");
        verify(userRepo).findById(user1.getUserId());
    }

    @Test
    public void testGetAll() {
        // Füge hier mehrere Benutzer hinzu und überprüfe, ob alle abgerufen werden können
        UserEntity newUser = new UserEntity();
        newUser.setUserId(3L);
        newUser.setUsername("testuser3");
        when(userRepo.findAll()).thenReturn(List.of(user1, user2, newUser));

        List<UserEntity> users = userService.getAll();

        // Überprüfe, ob alle Benutzer abgerufen wurden
        assertTrue(users.size() >= 2, "Nicht alle Benutzer wurden abgerufen");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser1")), "Benutzer 1 wurde nicht abgerufen");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("testuser2")), "Benutzer 2 wurde nicht abgerufen");
        verify(userRepo).findAll();
    }
}