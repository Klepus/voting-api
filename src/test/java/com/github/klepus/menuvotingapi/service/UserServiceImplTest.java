package com.github.klepus.menuvotingapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;

    @Test
    void loadUserByEmail() {
        String username = userService.loadUserByUsername("user@yandex.ru").getUsername();
        Assertions.assertEquals("user@yandex.ru", username);
    }

    @Test
    void loadUserByWrongEmail() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("wrong@mail.ru"));
    }
}