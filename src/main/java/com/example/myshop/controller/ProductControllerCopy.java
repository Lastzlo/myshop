package com.example.myshop.controller;

import com.example.myshop.domain.Product;
import com.example.myshop.exeptions.NotFoundExeption;
import com.example.myshop.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("product")
public class ProductControllerCopy {
    private final ProductRepo productRepo;


    @Autowired
    public ProductControllerCopy(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping
    public List<Product> list(){
        return productRepo.findAll();
    }

    @GetMapping("{id}")
    public Product getOne(@PathVariable String id){
        return productRepo.getOne(Long.valueOf(id));
    }


    @RequestMapping("create")
    @PostMapping
    private Product create(
            @RequestPart(value = "file") Optional<MultipartFile> file,
            @RequestPart(value = "product") Product product
    ){

        if (file.isPresent()) {
            System.out.println("fileName = "+file.get().getName());
        }

        return productRepo.save(product);
    }


    /*@PutMapping("{id}")
    public Map<String, String> update(
            @PathVariable String id,
            @RequestPart(value = "file") Optional<MultipartFile> file,
            @RequestPart(value = "product") Map<String, String> product
    ){
        Map<String, String> productFromDb = getProduct (id);

        productFromDb.putAll (product);

        if (file.isPresent()) {
            productFromDb.put ("file", file.get().getOriginalFilename ());
        }

        productFromDb.put ("id", id);

        return productFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Map<String, String> product = getProduct (id);

        products.remove (product);
    }*/



}
