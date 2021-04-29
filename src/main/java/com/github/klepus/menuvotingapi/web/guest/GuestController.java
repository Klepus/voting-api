package com.github.klepus.menuvotingapi.web.guest;

import com.github.klepus.menuvotingapi.model.Dish;
import com.github.klepus.menuvotingapi.model.Restaurant;
import com.github.klepus.menuvotingapi.repository.DishRepository;
import com.github.klepus.menuvotingapi.repository.RestaurantRepository;
import com.github.klepus.menuvotingapi.tos.MenuTo;
import com.github.klepus.menuvotingapi.tos.RestaurantTo;
import com.github.klepus.menuvotingapi.util.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.klepus.menuvotingapi.util.DateTimeUtil.checkAndInitStartDateEndDate;
import static com.github.klepus.menuvotingapi.util.ToUtil.*;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GuestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @Autowired
    public GuestController(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @ApiOperation(value = "Get restaurants list", notes = "Get all restaurants with info about them.")
    @Cacheable("listOfTos")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(GET_RESTAURANT_LIST)
    public List<RestaurantTo> findAll() {
        log.info("Get all restaurants");
        List<Restaurant> restaurants = restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        if (restaurants.isEmpty()) {
            return Collections.emptyList();
        } else {
            return allRestaurantTosCreate(restaurants);
        }
    }

    @ApiOperation(value = "Get menus list", notes = "Input startDate and endDate to get menus list of restaurants for provided dates. " +
            "If startDate and endDate not provided, will be shown menus on today.")
    @Cacheable("mapOfTos")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(GET_MENUS_LIST)
    public Map<LocalDate, List<MenuTo>> findAllMenus(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<LocalDate> list = checkAndInitStartDateEndDate(startDate, endDate);
        log.info("Get all menus for period: startDate={}, endDate={}", list.get(0), list.get(1));

        Optional<List<Dish>> dishesBetween = dishRepository.getDishesBetween(list.get(0), list.get(1));
        if (dishesBetween.isPresent()) {
            return allMenuTosCreate(dishesBetween.get());
        } else {
            return Collections.emptyMap();
        }
    }

    @ApiOperation(value = "Get single restaurant menu", notes = "Input startDate and endDate to get restaurant menu for provided dates. " +
            "If startDate and endDate not provided, will be shown menu on today.")
    @Cacheable("mapOfTos")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(GET_SINGLE_RESTAURANT_MENU)
    public Map<LocalDate, List<MenuTo>> getSingleRestaurantMenu(
            @PathVariable Integer id,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<LocalDate> list = checkAndInitStartDateEndDate(startDate, endDate);
        log.info("Get restaurant menu for period: startDate={}, endDate={}", list.get(0), list.get(1));

        Optional<List<Dish>> dishesBetween = dishRepository.getDishesBetweenSingleRestaurant(list.get(0), list.get(1), id);

        return allMenuTosCreate(dishesBetween.orElseThrow(() -> new NotFoundException("Restaurant menu not found")));
    }
}
