package com.example.myshop.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Set;

@Entity
public class LinkedDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    Long id;

    @JsonView(Views.IdName.class)
    String name;

    @OneToMany
    @JsonView(Views.OnlyChild.class)
    private Set<LinkedDirectory> children;

    @JsonView(Views.Type.class)
    String directoryType;

    @ManyToOne
    private LinkedDirectory father;


    public LinkedDirectory () {
    }

    public LinkedDirectory (String name, String directoryType) {
        this.name = name;
        this.directoryType = directoryType;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Set<LinkedDirectory> getChildren () {
        return children;
    }

    public void setChildren (Set<LinkedDirectory> children) {
        this.children = children;
    }

    public void addChild(LinkedDirectory child){
        this.children.add (child);
    }

    public void deleteChild(LinkedDirectory child){
        this.children.remove (child);
    }

    public String getDirectoryType () {
        return directoryType;
    }

    public void setDirectoryType (String directoryType) {
        this.directoryType = directoryType;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public LinkedDirectory getFather () {
        return father;
    }

    public void setFather (LinkedDirectory father) {
        this.father = father;
    }
}
