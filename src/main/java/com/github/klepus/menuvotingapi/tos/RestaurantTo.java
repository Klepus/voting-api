package com.github.klepus.menuvotingapi.tos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class RestaurantTo {
    private Integer id;

    @NotEmpty(message = "should not be empty")
    @Size(min = 2, max = 100, message = "should be in range 2 – 100 chars")
    @JsonProperty(value = "restaurant_name")
    private String name;

    @NotEmpty(message = "should not be empty")
    @JsonProperty(value = "restaurant_telephone")
    private String telephone;

    @NotEmpty(message = "should not be empty")
    @JsonProperty(value = "restaurant_address")
    private String address;

    public RestaurantTo(Integer id, @NotEmpty(message = "should not be empty") String name, String telephone, String address) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
    }
}
