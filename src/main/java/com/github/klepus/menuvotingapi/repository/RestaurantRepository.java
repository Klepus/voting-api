package com.github.klepus.menuvotingapi.repository;

import com.github.klepus.menuvotingapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r " +
            "JOIN FETCH r.dishes d " +
            "WHERE r.id = :id AND d.date = :today")
    Optional<Restaurant> getRestaurantWithDishesForToday(@Param("id") Integer id,
                                                         @Param("today") LocalDate today);

}
