package dev.lumme.reactivedemo.backend.entity;

import javax.persistence.Entity;

@Entity
public class City extends AbstractEntity {

    private String name;

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
