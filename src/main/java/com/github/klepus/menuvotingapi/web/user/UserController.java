package com.github.klepus.menuvotingapi.web.user;

import com.github.klepus.menuvotingapi.auth.AuthorizedUser;
import com.github.klepus.menuvotingapi.model.Restaurant;
import com.github.klepus.menuvotingapi.model.User;
import com.github.klepus.menuvotingapi.model.Vote;
import com.github.klepus.menuvotingapi.repository.RestaurantRepository;
import com.github.klepus.menuvotingapi.repository.UserRepository;
import com.github.klepus.menuvotingapi.repository.VoteRepository;
import com.github.klepus.menuvotingapi.tos.VoteTo;
import com.github.klepus.menuvotingapi.util.DateTimeUtil;
import com.github.klepus.menuvotingapi.util.exception.NotAllowedException;
import com.github.klepus.menuvotingapi.util.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.klepus.menuvotingapi.util.ToUtil.voteToCreate;
import static com.github.klepus.menuvotingapi.util.ToUtil.voteTosCreate;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.GET_USER_VOTES_HISTORY;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.POST_VOTE_FOR_RESTAURANT;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserController(RestaurantRepository restaurantRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "User votes history", authorizations = {@Authorization(value = "Basic")})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(GET_USER_VOTES_HISTORY)
    public List<VoteTo> getVotesHistory(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal AuthorizedUser authUser) {
        List<LocalDate> list = DateTimeUtil.checkAndInitStartDateEndDate(startDate, endDate);
        log.info("Get votes history, user id={}", authUser.getId());

        Optional<List<Vote>> findVotes = voteRepository.getAll(list.get(0), list.get(1), authUser.getId());
        if (findVotes.isPresent()){
            return voteTosCreate(findVotes.get());
        }else {
            return Collections.emptyList();
        }
    }

    @ApiOperation(value = "Vote for restaurant", authorizations = {@Authorization(value = "Basic")})
    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POST_VOTE_FOR_RESTAURANT)
    public ResponseEntity<VoteTo> voteForRestaurant(@PathVariable Integer id, @AuthenticationPrincipal AuthorizedUser authUser)  {
        log.info("Vote for restaurant with id={}", id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new NotFoundException("Restaurant with id=" + id + " not found"));
        User user = userRepository.getOne(authUser.getId());
        Optional<Vote> existedVote = voteRepository.findByUserIdAndDate(authUser.getId(), LocalDate.now());
        if (existedVote.isPresent()) {
            if (LocalTime.now().isAfter(DateTimeUtil.THRESHOLD_TIME)) {
                throw new NotAllowedException("Vote not accepted. Current time: " + LocalTime.now().format(DateTimeUtil.df) + " Votes accepting until 11:00 AM.");
            }
            Vote updatedVote = new Vote(user, restaurant);
            updatedVote.setId(existedVote.get().getId());
            return new ResponseEntity<>(voteToCreate(voteRepository.save(updatedVote)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(voteToCreate(voteRepository.save(new Vote(user, restaurant))), HttpStatus.CREATED);
        }
    }
}
