package com.github.klepus.menuvotingapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class Dish extends AbstractNamedEntity {
    private BigDecimal price;

    private Restaurant restaurant;

    private LocalDate date;

    public Dish(Integer id, String name, BigDecimal price, Restaurant restaurant, LocalDate localDate) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
        this.date = localDate;
    }

    @Override
    public String toString() {
        return "Dish: " + super.toString() + ", price=" + price + ", restaurant: " + " id=" + restaurant.getId() + ", name=" + restaurant.getName();
    }
}
