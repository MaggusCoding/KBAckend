package com.vocab.user_management_impl.factories;

import com.vocab.user_management.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFactory {

    public List<UserEntity> createUserListSize2(){
        return List.of(UserEntity.builder().username("max").build(), UserEntity.builder().username("moritz").build());
    }
}
