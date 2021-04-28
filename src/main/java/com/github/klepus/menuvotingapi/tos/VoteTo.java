package com.github.klepus.menuvotingapi.tos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class VoteTo {
    @JsonProperty(value = "vote_id")
    private int id;

    @NotNull
    @JsonProperty(value = "date_time")
    private LocalDateTime dateTime;

    @JsonProperty(value = "restaurant_id")
    private Integer restaurantId;

    @JsonProperty(value = "restaurant_name")
    private String restaurantName;

    public VoteTo(int id, @NotNull LocalDateTime dateTime, Integer restaurantId, String restaurant_name) {
        this.id = id;
        this.dateTime = dateTime;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurant_name;
    }
}
