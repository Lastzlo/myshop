package com.example.myshop.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class LinkedDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.IdName.class)
    private Long id;

    @JsonView(Views.IdName.class)
    private String name;

    @JsonView(Views.Type.class)
    private String directoryType;

    @OneToMany
    @JsonView(Views.OnlyChild.class)
    private Set<LinkedDirectory> children;

    @ManyToMany
    private Set<LinkedDirectory>relatedDirectories;


    //я знаю что данный сет частично повтрояет relatedDirectories, он сдела для того чтобы мы могли передать его в JSON и не столкнуться с рекурсией
    @ElementCollection
    @CollectionTable(
            name = "directories_associated_with_this_directory",
            joinColumns = @JoinColumn(name = "directory_id")
    )
    @Column(name = "related_directory")
    @JsonView(Views.FullLinkedDirectory.class)
    private Set<Long> relatedDirectoryIds = new HashSet<>();

    @ManyToOne
    private LinkedDirectory father;

    @ManyToMany
    //@JsonView(Views.FullMessage.class)
    private Set<Product> products;

    @JsonView(Views.FullLinkedDirectory.class)
    private Long productsCount;


    public LinkedDirectory () {
    }

    public LinkedDirectory (String name, String directoryType) {
        this.name = name;
        this.directoryType = directoryType;
        this.children = new HashSet<> ();

    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Set<Product> getProducts () {
        return products;
    }

    public void setProducts (Set<Product> products) {
        this.products = products;
    }

    public void addProduct (Product productFromDb) {
        this.products.add (productFromDb);
    }

    public void deleteProduct (Product productFromDb) {
        this.products.remove (productFromDb);
    }

    public Long getProductsCount () {
        return productsCount;
    }

    public void setProductsCount (Long productsCount) {
        this.productsCount = productsCount;
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

    public Set<LinkedDirectory> getRelatedDirectories () {
        return relatedDirectories;
    }

    public void setRelatedDirectories (Set<LinkedDirectory> relatedDirectories) {
        this.relatedDirectories = relatedDirectories;
    }

    public void addRelatedDirectory(LinkedDirectory relatedDirectory){
        this.relatedDirectories.add (relatedDirectory);
    }

    public void deleteRelatedDirectory(LinkedDirectory relatedDirectory){
        this.relatedDirectories.remove (relatedDirectory);
    }

    public Set<Long> getRelatedDirectoryIds () {
        return relatedDirectoryIds;
    }

    public void setRelatedDirectoryIds (Set<Long> relatedDirectoryIds) {
        this.relatedDirectoryIds = relatedDirectoryIds;
    }

    public void addRelatedDirectoryId(Long relatedDirectoryId){
        this.relatedDirectoryIds.add (relatedDirectoryId);
    }

    public void deleteRelatedDirectoryId(Long relatedDirectoryid){
        this.relatedDirectoryIds.remove (relatedDirectoryid);
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

    /*public Set<Product> getProducts () {
        return products;
    }

    public void setProducts (Set<Product> products) {
        this.products = products;
    }*/

    /*public void addProduct (Product product) {
        this.getProducts ().add (product);
    }*/
}
