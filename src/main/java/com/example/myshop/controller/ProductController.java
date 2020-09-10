package com.example.myshop.controller;

import com.example.myshop.domain.Product;
import com.example.myshop.domain.Views;
import com.example.myshop.repos.BrandRepo;
import com.example.myshop.repos.ProductRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {
    private final ProductRepo productRepo;

    private final BrandRepo brandRepo;

    @Autowired
    public ProductController (ProductRepo productRepo, BrandRepo brandRepo) {
        this.productRepo = productRepo;
        this.brandRepo = brandRepo;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Product> list(){
        return productRepo.findAll();
    }

    @GetMapping("{id}")
    public Product getOne(@PathVariable String id){
        return productRepo.getOne(Long.valueOf(id));
    }

    /*@PostMapping
    private Product create(
            @RequestPart(value = "file") Optional<MultipartFile> file,
            @RequestPart(value = "product") Product product
    ){
        if (file.isPresent()) {
            //на случай когда будут приходить картинки
            String fileName = file.get ().getOriginalFilename ();
            System.out.println("fileName = "+fileName);
            product.setProductFile (fileName);
        }

        return productRepo.save(product);
    }*/

    @PostMapping
    private Product create(
            @RequestBody Product product
    ){
        return productRepo.save(product);
    }

    /*@PutMapping("{id}")
    public Product update(
            @PathVariable String id,
            @RequestPart(value = "file") Optional<MultipartFile> file,
            @RequestPart(value = "product") Product product
    ){
        if (file.isPresent()) {
            //на случай когда будут приходить картинки
            String newFileName = file.get ().getOriginalFilename ();
            System.out.println("newFileName = "+newFileName);
            //product.setProductFile (newFileName);
        }

        Product productfromDb = productRepo.findById(Long.valueOf(id)).get();

        BeanUtils.copyProperties (product, productfromDb, "id");    //утила спринга которая копирует все поля из message в messageFromDb кроме id
        return productRepo.save (productfromDb);
    }*/

    @PostMapping("{id}")
    private Product update(
            @PathVariable String id,
            @RequestBody Product product
    ){

        Product productfromDb = productRepo.findById(Long.valueOf(id)).get();

        BeanUtils.copyProperties (product, productfromDb, "id");    //утила спринга которая копирует все поля из message в messageFromDb кроме id
        return productRepo.save (productfromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        productRepo.deleteById(Long.valueOf(id));
    }



}
