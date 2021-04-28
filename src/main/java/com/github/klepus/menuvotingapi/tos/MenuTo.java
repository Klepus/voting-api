package com.github.klepus.menuvotingapi.tos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MenuTo {
    @JsonIgnore
    private LocalDate date;

    @JsonProperty(value = "restaurant_id")
    private Integer id;

    @JsonProperty(value = "restaurant_name")
    private String name;

    @JsonProperty(value = "restaurant_menu")
    private Set<DishTo> dishes;

    public MenuTo(Integer id, String name, @NotNull LocalDate date, Set<DishTo> dishes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.dishes = dishes;
    }

    public MenuTo(LocalDate date, Integer id, String name) {
        this.date = date;
        this.id = id;
        this.name = name;
    }

    @JsonProperty(value = "restaurant_menu")
    public Set<DishTo> getDishes() {
        return dishes;
    }
}
