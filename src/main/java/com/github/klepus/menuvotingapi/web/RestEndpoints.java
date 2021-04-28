package com.github.klepus.menuvotingapi.web;

public class RestEndpoints {
    private RestEndpoints() {}
    public static final String
    // All users
    GET_RESTAURANT_LIST =            "/api/restaurants",
    GET_MENUS_LIST =                 "/api/restaurants/menus",
    GET_SINGLE_RESTAURANT_MENU =     "/api/restaurants/{id}",

    // Authorised users
    GET_USER_VOTES_HISTORY =         "/api/profile/votes",
    POST_VOTE_FOR_RESTAURANT =       "/api/profile/restaurants/{id}",

    // Admin
    POST_ADMIN_CREATE_RESTAURANT =   "/api/admin/restaurants",
    POST_ADMIN_CREATE_MENU =         "/api/admin/restaurants/{id}/menu",
    PUT_UPDATE_RESTAURANT_INFO =     "/api/admin/restaurants/{id}",
    PUT_UPDATE_MENU =                "/api/admin/restaurants/{id}/menu",
    DELETE_RESTAURANT =              "/api/admin/restaurants/{id}",
    DELETE_MENU =                    "/api/admin/restaurants/{id}/menu";

}
