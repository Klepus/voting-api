package com.github.klepus.menuvotingapi.repository;

import com.github.klepus.menuvotingapi.model.Restaurant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RestaurantRepositoryTest {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void findAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        Assertions.assertEquals(3, restaurants.size());
    }

}