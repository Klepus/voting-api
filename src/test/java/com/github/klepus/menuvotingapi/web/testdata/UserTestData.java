package com.github.klepus.menuvotingapi.web.testdata;


import com.github.klepus.menuvotingapi.model.Role;
import com.github.klepus.menuvotingapi.model.User;

public final class UserTestData {
    private UserTestData() {}

    public static final User USER = new User(
            1,
            "User",
            "user@yandex.ru",
            "$2a$10$AQ/mfRr3KuZx1vX4bruzquumXgLaZul4OKzoSZOr4Zd8cbiq4idRK",
            null,
            Role.ROLE_USER
    );
    public static final User SECOND_USER = new User(
            3,
            "Second User",
            "seconduser@yandex.ru",
            "$2a$10$AQ/mfRr3KuZx1vX4bruzquumXgLaZul4OKzoSZOr4Zd8cbiq4idRK",
            null,
            Role.ROLE_USER
    );
    public static final User ADMIN = new User(
            2,
            "Admin",
            "admin@gmail.com",
            "$2a$10$lcXKWK.Wq9UpRWDhea3V2uVr2loBbSUCZogp/MIQFrDbKZYWNuK.C",
            null,
            Role.ROLE_ADMIN
    );

    public static final String ADMIN_PASSWORD = "admin";
    public static final String USER_PASSWORD = "password";

}
