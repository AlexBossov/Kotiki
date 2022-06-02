package ru.itmo.kotiki.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.itmo.kotiki.enums.Color;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "cats")
@Entity
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Enumerated
    @Column(name = "color", nullable = false)
    private Color color;

    @Column(name = "breed", nullable = false)
    private String breed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "cat_cats",
            joinColumns = @JoinColumn(name = "cat_1_id"),
            inverseJoinColumns = @JoinColumn(name = "cats_2_id"))
    private List<Cat> cats = new ArrayList<>();

    public Cat() {
    }

    public Cat(Long id, String name, LocalDate birthday, Color color, String breed, Owner owner, List<Cat> cats) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.color = color;
        this.breed = breed;
        this.owner = owner;
        this.cats = cats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }
}