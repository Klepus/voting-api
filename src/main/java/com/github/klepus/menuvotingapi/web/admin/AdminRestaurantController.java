package com.github.klepus.menuvotingapi.web.admin;

import com.github.klepus.menuvotingapi.model.Dish;
import com.github.klepus.menuvotingapi.model.Restaurant;
import com.github.klepus.menuvotingapi.repository.*;
import com.github.klepus.menuvotingapi.tos.*;
import com.github.klepus.menuvotingapi.util.exception.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.github.klepus.menuvotingapi.util.ToUtil.*;
import static com.github.klepus.menuvotingapi.util.ValidationUtil.*;
import static com.github.klepus.menuvotingapi.web.RestEndpoints.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @Autowired
    public AdminRestaurantController(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @ApiOperation(value = "Create restaurant", authorizations = {@Authorization(value = "Basic")})
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = POST_ADMIN_CREATE_RESTAURANT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createRestaurant(@RequestBody @Valid RestaurantTo restaurantTo, BindingResult r) {
        log.info("Create restaurant with name='{}'", restaurantTo.getName());
        fieldValidation(r);

        Restaurant restaurant = restaurantCreate(restaurantTo);
        Restaurant created = restaurantRepository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(POST_ADMIN_CREATE_RESTAURANT + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(
                restaurantToCreate(restaurant)
        );
    }

    @ApiOperation(value = "Update restaurant", authorizations = {@Authorization(value = "Basic")})
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = PUT_UPDATE_RESTAURANT_INFO, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> updateRestaurant(@RequestBody @Valid RestaurantTo restaurantTo, @PathVariable int id, BindingResult r) {
        log.info("Update restaurant with id={}", id);
        fieldValidation(r);

        Restaurant existRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found."));
        if (restaurantTo.getName() != null) existRestaurant.setName(restaurantTo.getName());
        if (restaurantTo.getAddress() != null) existRestaurant.setAddress(restaurantTo.getAddress());
        if (restaurantTo.getTelephone() != null) existRestaurant.setTelephone(restaurantTo.getTelephone());

        return ResponseEntity.ok(
                restaurantToCreate(restaurantRepository.save(existRestaurant))
        );
    }

    @ApiOperation(value = "Delete restaurant", authorizations = {@Authorization(value = "Basic")})
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = DELETE_RESTAURANT)
    public void deleteRestaurant(@PathVariable int id) {
        log.info("Delete restaurant with id={}", id);
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    @ApiOperation(value = "Create Menu", notes = "Create restaurants menu for today", authorizations = {@Authorization(value = "Basic")})
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = POST_ADMIN_CREATE_MENU, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<LocalDate, List<MenuTo>>> createMenu(@PathVariable Integer id, @RequestBody @Valid MenuTo menuTo, BindingResult r) {
        log.info("Create menu for restaurant with id={}", id);
        fieldValidation(r);

        Optional<List<Dish>> dishesForToday = dishRepository.getDishesBetweenSingleRestaurant(LocalDate.now(), LocalDate.now(), id);
        if (dishesForToday.isEmpty()) {
            Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getOne(id), id);
            Set<Dish> result = dishesCreate(menuTo.getDishes(), restaurant);
            List<Dish> dishes = dishRepository.saveAll(result);

            return new ResponseEntity<>(allMenuTosCreate(dishes), HttpStatus.CREATED);
        } else {
            throw new ForbiddenException("Menu for today is persisted already. Save again not allowed. " +
                    "You can update current menu, or delete and than persist new menu.");
        }
    }

    @ApiOperation(value = "Update Menu", notes = "Update restaurants menu for today", authorizations = {@Authorization(value = "Basic")})
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = PUT_UPDATE_MENU, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<LocalDate, List<MenuTo>>> updateMenu(@PathVariable Integer id, @RequestBody @Valid MenuTo menuTo, BindingResult r) {
        log.info("Update menu for restaurant with id={}", id);
        fieldValidation(r);

        Optional<Restaurant> restaurant = restaurantRepository.getRestaurantWithDishesForToday(id, LocalDate.now());

        if (restaurant.isEmpty()) {
            throw new NotFoundException("Update not allowed. Menu for today not found. You have to persist new menu.");
        } else {
            dishRepository.deleteAll(restaurant.get().getDishes());
            Set<Dish> newMenu = dishesCreate(menuTo.getDishes(), restaurant.get());

            return new ResponseEntity<>(allMenuTosCreate(dishRepository.saveAll(newMenu)), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Delete menu", notes = "Delete menu for today by id", authorizations = {@Authorization(value = "Basic")})
    @CacheEvict(cacheNames = { "listOfTos", "mapOfTos" }, allEntries = true)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = DELETE_MENU)
    public void deleteMenu(@PathVariable Integer id) {
        log.info("Delete menu for restaurant with id={}", id);

        if (!restaurantRepository.existsById(id)) {
            throw new NotFoundException("Delete not allowed. Restaurant with id=" + id + " not found.");
        }

        Optional<Restaurant> restaurant = restaurantRepository.getRestaurantWithDishesForToday(id, LocalDate.now());
        if (restaurant.isEmpty()) {
            throw new NotFoundException("Delete not allowed. Menu not found.");
        } else {
            dishRepository.deleteAll(restaurant.get().getDishes());
        }
    }
}
