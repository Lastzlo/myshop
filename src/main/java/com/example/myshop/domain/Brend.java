package com.example.myshop.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Brend {
    @Id
    Long id;

    String brendName;

    public Brend() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrendName() {
        return brendName;
    }

    public void setBrendName(String brendName) {
        this.brendName = brendName;
    }
}
