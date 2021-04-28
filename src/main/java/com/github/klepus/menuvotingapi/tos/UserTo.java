package com.github.klepus.menuvotingapi.tos;


import com.github.klepus.menuvotingapi.model.Role;
import com.github.klepus.menuvotingapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserTo {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private boolean enabled;
    private Date registered;
    private Set<Role> roles;

    public UserTo(User user) {
        this(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                true,
                user.getRegistered(),
                user.getRoles()
        );
    }

    @Override
    public String toString() {
        return "UserTo: " + "id=" + id + ", name=" + name + ", email=" + email + ", registered=" + registered +
                ", roles=" + roles;
    }
}
