package com.vocab.user_management.repos;

import com.vocab.user_management.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<UserEntity> findByUsername(String username);
}
