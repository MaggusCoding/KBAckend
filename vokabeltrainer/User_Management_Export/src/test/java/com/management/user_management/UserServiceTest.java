package com.management.user_management;

import com.management.user_management.entities.User;
import com.management.user_management.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class UserServiceTest {

    UserServiceImpl service = new UserServiceImpl();

    @Test
    void testCreateUserExpectOk() throws UserAlreadyExistsException {
        // Arrange
        User user = new User(0L, "test");

        // Act
        service.createUser(user);

        // Assert
        assertThat(service.getAll()).isNotEmpty().hasSize(1);

    }

    @Test
    void testGetByIdExpectOk() throws UserAlreadyExistsException {
        // Arrange
        User user = new User(0L, "test");

        // Act
        service.createUser(user);

        // Assert
        assertThat(service.getById(0L)).isNotNull();
        assertThat(service.getById(0L).getUsername()).isEqualTo("test");

    }

    @Test
    void testCreateSameUserExpectException() throws UserAlreadyExistsException {
        // Arrange
        User user1 = new User(0L, "user1");
        service.createUser(user1);

        // Act & Assert
        assertThatThrownBy(() -> service.createUser(user1)).isInstanceOf(UserAlreadyExistsException.class);
    }

}
