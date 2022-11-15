package com.training.springbatch1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coffee")
public class Coffee {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "blend")
    private String blend;
    @Column(name = "strength")
    private String strength;
    @Column(name="origin")
    private String origin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlend() {
        return blend;
    }

    public void setBlend(String blend) {
        this.blend = blend;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "Coffee info: " +
                "blend='" + blend + '\'' +
                ", strength='" + strength + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}
