package com.github.klepus.menuvotingapi.util;

import com.github.klepus.menuvotingapi.model.Dish;
import com.github.klepus.menuvotingapi.model.Restaurant;
import com.github.klepus.menuvotingapi.model.Vote;
import com.github.klepus.menuvotingapi.tos.DishTo;
import com.github.klepus.menuvotingapi.tos.MenuTo;
import com.github.klepus.menuvotingapi.tos.RestaurantTo;
import com.github.klepus.menuvotingapi.tos.VoteTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class ToUtil {

    private ToUtil() {
    }

    public static List<RestaurantTo> createTosFromRestaurants(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(e-> new RestaurantTo(e.getId(), e.getName(), e.getTelephone(), e.getAddress()))
                .collect(Collectors.toList());
    }

    public static Restaurant createRestaurantFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(null, restaurantTo.getName(), Collections.emptySet(), restaurantTo.getTelephone(), restaurantTo.getAddress());
    }

    public static RestaurantTo createToFromRestaurant(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getTelephone(), restaurant.getAddress());
    }

    public static Map<LocalDate, List<MenuTo>> createTosFromMenus(List<Dish> dishes) {
        Set<MenuTo> menuToList = dishes.stream()
                .map(e -> new MenuTo(e.getDate(), e.getRestaurant().getId(), e.getRestaurant().getName()))
                .collect(Collectors.toSet());

        List<MenuTo> menuTos = menuToList.stream()
                .peek(e -> e.setDishes(filterDishes(e, dishes)))
                .sorted(Comparator.comparing(MenuTo::getId))
                .collect(Collectors.toList());

        return menuTos.stream().collect(Collectors.groupingBy(MenuTo::getDate));
    }

    private static Set<DishTo> filterDishes(MenuTo menuTo, List<Dish> dishes) {
        List<Dish> disheList = dishes.stream()
                .filter(e -> e.getDate().equals(menuTo.getDate()))
                .filter(e -> e.getRestaurant().getName().equals(menuTo.getName()))
                .collect(Collectors.toList());

        return disheList.stream()
                .map(e -> new DishTo(e.getName(), e.getPrice()))
                .collect(Collectors.toSet());
    }

    public static Set<Dish> createDishesFromTo(Set<DishTo> dishes, Restaurant restaurant) {
        return dishes.stream()
                .map(e -> new Dish(null, e.getName(), e.getPrice(), restaurant, LocalDate.now()))
                .collect(Collectors.toSet());
    }

    public static List<VoteTo> createTosFromVotes(List<Vote> votes) {
        return votes.stream()
                .map(ToUtil::createToFromVote)
                .collect(Collectors.toList());
    }

    public static VoteTo createToFromVote(Vote vote) {
        return new VoteTo(vote.getId(), LocalDateTime.of(vote.getDate(), vote.getTime()), vote.getRestaurant().getId(), vote.getRestaurant().getName());
    }
}
