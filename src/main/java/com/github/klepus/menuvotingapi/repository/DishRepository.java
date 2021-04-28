package com.github.klepus.menuvotingapi.repository;

import com.github.klepus.menuvotingapi.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Query("SELECT d FROM Dish d " +
            "JOIN FETCH d.restaurant r " +
            "WHERE d.date >= :startDate AND d.date <= :endDate")
    Optional<List<Dish>> getDishesBetween(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);

    @Query("SELECT d FROM Dish d " +
            "JOIN FETCH d.restaurant r " +
            "WHERE d.date >= :startDate AND d.date <= :endDate AND d.restaurant.id = :id")
    Optional<List<Dish>> getDishesBetweenSingleRestaurant(@Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate,
                                                          @Param("id") Integer id);
}
