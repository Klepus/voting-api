package com.github.klepus.menuvotingapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class Vote extends AbstractBaseEntity {
    private LocalDate date;

    private LocalTime time;

    private User user;

    private String userEmail;

    private Restaurant restaurant;

    private String restaurantName;

    public Vote(Restaurant restaurant) {
        super(null);
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.user = user;
        this.userEmail = user.getEmail();
        this.restaurant = restaurant;
        this.restaurantName = restaurant.getName();
    }

    public Vote(Integer id, Restaurant restaurant, LocalDate date, LocalTime time) {
        super(id);
        this.user = user;
        this.userEmail = user.getEmail();
        this.restaurant = restaurant;
        this.restaurantName = restaurant.getName();
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return super.toString() + ", date=" + date + ", time=" + time + ", user id=" + user.getId() + ", restaurant id=" + restaurant.getId();
    }
}
