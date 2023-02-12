package com.example.chatlogs.services;

import com.example.chatlogs.models.User;

public interface UserService {

    User findById(Long id);
}
