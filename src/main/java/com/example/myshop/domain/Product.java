package com.example.myshop.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String productName;

    //brand
    // tag
    // dicription
    // price
    // photos
    @JsonView(Views.FullMessage.class)
    private String productDiscription;

    @ElementCollection
    @JsonView(Views.FullMessage.class)
    private List<String> photos;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany
    @JsonView(Views.FullMessage.class)
    private Set<Tag> tags;

    @JsonView(Views.FullMessage.class)
    private String price;

    public Product() {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getProductName () {
        return productName;
    }

    public void setProductName (String productName) {
        this.productName = productName;
    }

    public String getProductDiscription () {
        return productDiscription;
    }

    public void setProductDiscription (String productDiscription) {
        this.productDiscription = productDiscription;
    }

    public List<String> getPhotos () {
        return photos;
    }

    public void setPhotos (List<String> photos) {
        this.photos = photos;
    }

    public Category getCategory () {
        return category;
    }

    public void setCategory (Category category) {
        this.category = category;
    }

    public Brand getBrand () {
        return brand;
    }

    public void setBrand (Brand brand) {
        this.brand = brand;
    }

    public Set<Tag> getTags () {
        return tags;
    }

    public void setTags (Set<Tag> tags) {
        this.tags = tags;
    }

    public String getPrice () {
        return price;
    }

    public void setPrice (String price) {
        this.price = price;
    }
}
