package com.github.klepus.menuvotingapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class Restaurant extends AbstractNamedEntity {
    private String telephone;

    private String address;

    private Set<Dish> dishes = new HashSet<>();

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Integer id, String name, Set<Dish> dishes) {
        this(id, name);
        this.dishes = dishes;
    }

    public Restaurant(Integer id, String name, Set<Dish> dishes, String telephone, String address) {
        this(id, name, dishes);
        this.telephone = telephone;
        this.address = address;
    }

    @Override
    public String toString() {
        return super.toString() + ", telephone=" + telephone + ", address=" + address + ", menus:\n" + dishes;
    }
}
