package com.vocab.user_management_impl;

import com.management.user_management.UserAlreadyExistsException;
import com.management.user_management.entities.UserEntity;
import com.vocab.user_management_impl.services.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserServiceTest {

    UserServiceImpl service = new UserServiceImpl();

    @Test
    void testCreateUserExpectOk() throws UserAlreadyExistsException {
        // Arrange
        UserEntity userEntity = new UserEntity(0L, "test");

        // Act
        service.createUser(userEntity);

        // Assert
        Assertions.assertThat(service.getAll()).isNotEmpty().hasSize(1);

    }

    @Test
    void testGetByIdExpectOk() throws UserAlreadyExistsException {
        // Arrange
        UserEntity userEntity = new UserEntity(0L, "test");

        // Act
        service.createUser(userEntity);

        // Assert
        Assertions.assertThat(service.getById(0L)).isNotNull();
        Assertions.assertThat(service.getById(0L).getUsername()).isEqualTo("test");

    }

    @Test
    void testCreateSameUserExpectException() throws UserAlreadyExistsException {
        // Arrange
        UserEntity userEntity1 = new UserEntity(0L, "user1");
        service.createUser(userEntity1);

        // Act & Assert
        assertThatThrownBy(() -> service.createUser(userEntity1)).isInstanceOf(UserAlreadyExistsException.class);
    }

}
