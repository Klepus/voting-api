package com.github.klepus.menuvotingapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
public class User extends AbstractNamedEntity {
    private String email;

    private String password;

    private Date registered = new Date();

    private Set<Role> roles;

    private List<Vote> votes;

    public User(Integer id, String name, String email, String password, Date registered, Set<Role> roles, List<Vote> votes) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.registered = registered;
        this.roles = roles;
        this.votes = votes;
    }

    public User(Integer id, String name, String email, String password, List<Vote> votes, Role role, Role... roles) {
        this(id, name, email, password, new Date(), EnumSet.of(role, roles), votes);
    }

    @Override
    public String toString() {
        return super.toString() + ", email=" + email + ", registered=" + registered + ", roles=" + roles +
                ", votes=" + votes;
    }
}