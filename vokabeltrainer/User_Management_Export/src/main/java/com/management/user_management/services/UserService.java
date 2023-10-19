package com.management.user_management.services;

import com.management.user_management.entities.User;
import java.util.List;

public interface UserService {

    User createUser(User user);

    User getById(Long id);

    List<User> getAll();

}
