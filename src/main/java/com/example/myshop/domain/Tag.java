package com.example.myshop.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String tagName;

    @ManyToMany
    private Set<Product> products;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Tag () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getTagName () {
        return tagName;
    }

    public void setTagName (String tagName) {
        this.tagName = tagName;
    }

    public Set<Product> getProducts () {
        return products;
    }

    public void setProducts (Set<Product> products) {
        this.products = products;
    }

    public Category getCategory () {
        return category;
    }

    public void setCategory (Category category) {
        this.category = category;
    }
}
