package com.example.myshop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productName;

    @ElementCollection
    private List<String> photos;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ElementCollection
    private List<String> tags;

    private String price;

    private String productDiscription;

    //brand
    // tag dicription price photos


    public Product (String productName, List<String> photos, Brand brand, List<String> tags, String price, String productDiscription) {
        this.productName = productName;
        this.photos = photos;
        this.brand = brand;
        this.tags = tags;
        this.price = price;
        this.productDiscription = productDiscription;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        ArrayList<String> strings = new ArrayList<> ();
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getPhotos () {
        return photos;
    }

    public void setPhotos (List<String> photos) {
        this.photos = photos;
    }

    public void addPhotoSrc (String photoSrc){
        this.photos.add (photoSrc);
    }


    public Brand getBrand () {
        return brand;
    }

    public void setBrand (Brand brand) {
        this.brand = brand;
    }

    public List<String> getTags () {
        return tags;
    }

    public void setTags (List<String> tags) {
        this.tags = tags;
    }

    public void setTag (String tag){
        this.tags.add (tag);
    }

    public String getPrice () {
        return price;
    }

    public void setPrice (String price) {
        this.price = price;
    }

    public String getProductDiscription () {
        return productDiscription;
    }

    public void setProductDiscription (String productDiscription) {
        this.productDiscription = productDiscription;
    }
}
