package com.github.klepus.menuvotingapi.tos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class DishTo {
    @NotEmpty(message = "should not be empty")
    @Size(min = 2, max = 100, message = "should be in range 2 – 100 chars")
    @JsonProperty(value = "dish_name")
    private String name;

    @JsonProperty(value = "dish_price")
    private BigDecimal price;

    public DishTo(@NotEmpty(message = "should not be empty") String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
