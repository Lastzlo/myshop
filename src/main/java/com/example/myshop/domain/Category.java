package com.example.myshop.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    Long id;
    @JsonView(Views.IdName.class)
    String categoryName;

    //products
    //brands
    //tags

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private Set<Product> products;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    @JsonView(Views.Brands.class)
    private Set<Brand> brands;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private Set<Tag> tags;


    public Category() {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getCategoryName () {
        return categoryName;
    }

    public void setCategoryName (String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Product> getProducts () {
        return products;
    }

    public void setProducts (Set<Product> products) {
        this.products = products;
    }

    public Set<Brand> getBrands () {
        return brands;
    }

    public void setBrands (Set<Brand> brands) {
        this.brands = brands;
    }

    public Set<Tag> getTags () {
        return tags;
    }

    public void setTags (Set<Tag> tags) {
        this.tags = tags;
    }
}
