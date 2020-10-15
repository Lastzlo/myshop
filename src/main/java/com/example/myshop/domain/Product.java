package com.example.myshop.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    //PROBLEM! lengh only 255 charts
    @JsonView(Views.FullMessage.class)
    private String productDiscription;

    @OneToMany
    @JsonView(Views.FullMessage.class)
    private Set<Photo> photos;

    @Transient
    private Set<Photo> photoToDelete;


//    @JsonView(Views.FullMessage.class)
//    private String filename;

    @ManyToMany
    @JsonView(Views.FullMessage.class)
    private Set<LinkedDirectory> tags;

    @JsonView(Views.FullMessage.class)
    private String price;

    @Column(updatable = false)  //колонка не обновляемая
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")   //чтобы Джейсон отшорматировал  дату и время по нашему патерну
    @JsonView(Views.FullMessage.class)
    private LocalDateTime creationDate;

    public Product () {
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


    public Set<Photo> getPhotos () {
        return photos;
    }

    public void setPhotos (Set<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto (Photo photo) {
        this.photos.add (photo);
    }

    public void deletePhoto(Photo photo){
        this.photos.remove (photo);
    }


    public Set<LinkedDirectory> getTags () {
        return tags;
    }

    public void setTags (Set<LinkedDirectory> tags) {
        this.tags = tags;
    }

    public void addTag (LinkedDirectory tagFromDb) {
        this.tags.add (tagFromDb);
    }

    public void deleteTag (LinkedDirectory tagFromDb) {
        this.tags.remove (tagFromDb);
    }

    public String getPrice () {
        return price;
    }

    public void setPrice (String price) {
        this.price = price;
    }

    public LocalDateTime getCreationDate () {
        return creationDate;
    }

    public void setCreationDate (LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Photo> getPhotoToDelete () {
        return photoToDelete;
    }

}
