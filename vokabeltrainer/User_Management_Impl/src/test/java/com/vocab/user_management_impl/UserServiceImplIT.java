package com.vocab.user_management_impl;

import com.vocab.user_management.entities.UserEntity;
import com.vocab.user_management_impl.repos.UserRepo;
import com.vocab.user_management_impl.services.UserServiceImpl;
import com.vocab.vocabulary_duel_impl.repos.DuelRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// credit to: https://blog.mimacom.com/testing-optimistic-locking-handling-spring-boot-jpa/
@SpringBootTest
public class UserServiceImplIT {

    @SpyBean
    private UserServiceImpl userService;

    @SpyBean
    UserRepo userRepo;

    @SpyBean
    DuelRepo duelRepo;

    @Test
    public void testDeleteUserOptimisticLockHandling() throws InterruptedException {
        // given
        UserEntity user1 = userService.createUser("user1");
        AtomicBoolean deleteSuccess1 = new AtomicBoolean(false);
        AtomicBoolean deleteSuccess2 = new AtomicBoolean(false);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        // then
        executor.execute(() -> deleteSuccess1.set(userService.deleteUser(user1.getUserId())));
        executor.execute(() -> deleteSuccess2.set(userService.deleteUser(user1.getUserId())));

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

//        System.out.println("deleteSuccess1=" + deleteSuccess1.get() + "; deleteSuccess2=" + deleteSuccess2.get());
        // user1 was intended to delete in 2 threads but one of the thread was aborted due to optimistic locking
        assertNotEquals(deleteSuccess1.get(), deleteSuccess2.get());
        verify(userService, times(2)).deleteUser(user1.getUserId());
        verify(userRepo, times(2)).deleteById(user1.getUserId());
        verify(userRepo, times(2)).existsById(user1.getUserId());
        verify(duelRepo, times(2)).existsDuelByPlayersIsContaining(user1);
    }
}
