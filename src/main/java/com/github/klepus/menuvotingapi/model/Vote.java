package com.github.klepus.menuvotingapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"date", "time"}, name = "vote_uniq_date_time_idx"),
        @UniqueConstraint(columnNames = {"user_email_history"}, name = "vote_uniq_user_idx"),
        @UniqueConstraint(columnNames = {"restaurant_name_history"}, name = "vote_uniq_restaurant_idx"),
})
public class Vote extends AbstractBaseEntity {
    @Column(name = "date", nullable = false, columnDefinition = "timestamp default current_date()")
    private LocalDate date;

    @Column(name = "time", nullable = false, columnDefinition = "timestamp default current_time()")
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_email_history")
    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "restaurant_name_history")
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
