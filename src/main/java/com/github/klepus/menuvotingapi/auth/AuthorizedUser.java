package com.github.klepus.menuvotingapi.auth;

import com.github.klepus.menuvotingapi.model.User;
import com.github.klepus.menuvotingapi.tos.UserTo;


public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private final UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true,
                true, true, true, user.getRoles());
        this.userTo = new UserTo(user);
    }

    public Integer getId() {
        return userTo.getId();
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}