package com.github.klepus.menuvotingapi.web;

public class RestEndpoints {
    private RestEndpoints() {}
    public static final String
    // All users
    GET_ALL_RESTAURANTS =                "/api/restaurants",
    GET_ALL_CURRENT_MENUS =              "/api/restaurants/menus",
    GET_CURRENT_RESTAURANT_MENU_BY_ID =  "/api/restaurants/{id}",
    GET_CURRENT_VOTES =                  "/api/votes",

    // Authorised users
    POST_VOTE_FOR_RESTAURANT =      "/api/profile/restaurants/{id}",

    // Admin
    POST_ADMIN_CREATE_RESTAURANT =    "/api/admin/restaurants",
    POST_ADMIN_CREATE_CURRENT_MENU =  "/api/admin/restaurants/{id}/menu",
    PUT_UPDATE_RESTAURANT_INFO =      "/api/admin/restaurants/{id}",
    PUT_UPDATE_CURRENT_MENU =         "/api/admin/restaurants/{id}/menu",
    DELETE_RESTAURANT =               "/api/admin/restaurants/{id}",
    DELETE_CURRENT_MENU =             "/api/admin/restaurants/{id}/menu";

}
