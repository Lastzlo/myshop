package com.example.myshop.controller;

import com.example.myshop.domain.Product;
import com.example.myshop.domain.Views;
import com.example.myshop.services.GeneralService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private GeneralService generalService;

    @GetMapping
    @JsonView(Views.FullMessage.class)
    public List<Product> list(){
        return generalService.getAllProducts();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Product getOne(@PathVariable String id){
        return generalService.getProduct(Long.valueOf(id));
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
    @JsonView(Views.FullMessage.class)
    private Product create(
            @RequestBody Product product
    ){
        return generalService.saveProduct(product);
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

    /*@PostMapping("{id}")
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
    }*/



}
