package com.github.klepus.menuvotingapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User extends AbstractNamedEntity {
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registered", columnDefinition = "timestamp default now()")
    private Date registered = new Date();

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("date DESC, time DESC")
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