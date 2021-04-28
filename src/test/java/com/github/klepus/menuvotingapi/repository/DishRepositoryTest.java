package com.github.klepus.menuvotingapi.repository;

import com.github.klepus.menuvotingapi.model.Dish;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DishRepositoryTest {
    @Autowired
    DishRepository dishRepository;

    @Test
    void findAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        Assertions.assertEquals(24, dishes.size());
    }

}