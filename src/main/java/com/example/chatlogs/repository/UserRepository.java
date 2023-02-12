package com.example.chatlogs.repository;

import com.example.chatlogs.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
