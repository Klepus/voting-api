package com.github.klepus.menuvotingapi.model;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractNamedEntity extends AbstractBaseEntity {
    private String name;

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + ", name=" + name;
    }
}
