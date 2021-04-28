package com.github.klepus.menuvotingapi.repository;

import com.github.klepus.menuvotingapi.model.Vote;
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
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v " +
            "JOIN FETCH v.restaurant " +
            "JOIN FETCH v.user u " +
            "WHERE v.date >= :startDate AND v.date <= :endDate AND u.id= :userId " +
            "ORDER BY v.date DESC, u.id")
    Optional<List<Vote>> getAll(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("userId") Integer userId);

    Optional<Vote> findByUserIdAndDate(Integer id, LocalDate date);
}
