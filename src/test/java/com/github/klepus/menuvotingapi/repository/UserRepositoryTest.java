package com.github.klepus.menuvotingapi.repository;

import com.github.klepus.menuvotingapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void findAllUsers() {
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(3, users.size());
    }
}
