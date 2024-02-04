package com.vocab.user_management.dtos;

import com.vocab.user_management.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;

    public static UserDTO fromEntity(UserEntity user){
        UserDTO dto = new UserDTO();
        dto.userId = user.getUserId();
        dto.username = user.getUsername();
        return dto;
    }

}
