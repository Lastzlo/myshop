package com.example.myshop.controller;

import com.example.myshop.domain.Product;
import com.example.myshop.exeptions.NotFoundExeption;
import com.example.myshop.repos.ProductRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("product")
public class ProductController {
    private final ProductRepo productRepo;


    @Autowired
    public ProductController(ProductRepo productRepo) {
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

    @PostMapping
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
    }


    @PutMapping("{id}")
    public Product update(
            @PathVariable String id,
            @RequestPart(value = "file") Optional<MultipartFile> file,
            @RequestPart(value = "product") Product product
    ){
        if (file.isPresent()) {
            //на случай когда будут приходить картинки
            String newFileName = file.get ().getOriginalFilename ();
            System.out.println("newFileName = "+newFileName);
            product.setProductFile (newFileName);
        }

        Product productfromDb = productRepo.findById(Long.valueOf(id)).get();

        BeanUtils.copyProperties (product, productfromDb, "id");    //утила спринга которая копирует все поля из message в messageFromDb кроме id
        Product updatedProduct = productRepo.save (productfromDb);
        return updatedProduct;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        productRepo.deleteById(Long.valueOf(id));
    }



}
