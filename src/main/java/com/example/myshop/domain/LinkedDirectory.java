package com.example.myshop.domain;



import javax.persistence.*;
import java.util.Set;

@Entity
public class LinkedDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    String directoryType;

    @ManyToOne
    private LinkedDirectory father;

    @OneToMany
    private Set<LinkedDirectory> children;

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
