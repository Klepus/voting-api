package com.github.klepus.menuvotingapi.repository;

import com.github.klepus.menuvotingapi.model.Vote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VoteRepositoryTest {
    @Autowired
    VoteRepository voteRepository;

    @Test
    void findAllVotes() {
        List<Vote> votes = voteRepository.findAll();
        Assertions.assertEquals(6, votes.size());
    }
}
