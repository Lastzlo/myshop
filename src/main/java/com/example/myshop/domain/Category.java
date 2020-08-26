package com.example.myshop.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Category {
    @Id
    Long id;

    String categoryName;

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
