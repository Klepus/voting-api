package com.github.klepus.menuvotingapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedEntity {
    @Column(name = "telephone")
    private String telephone;

    @Column(name = "address")
    private String address;

    @OneToMany(
            mappedBy = "restaurant",
            fetch = FetchType.LAZY)
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
