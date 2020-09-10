package com.example.myshop.controller;

import com.example.myshop.domain.Brand;
import com.example.myshop.domain.Views;
import com.example.myshop.repos.BrandRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    private final BrandRepo brandRepo;


    @Autowired
    public BrandController (BrandRepo brandRepo) {
        this.brandRepo = brandRepo;
    }

    @GetMapping
    @JsonView({Views.IdName.class})
    public List<Brand> list(){
        return brandRepo.findAll();
    }

    @GetMapping("{id}")
    public Brand getOne(@PathVariable String id){
        return brandRepo.getOne(Long.valueOf(id));
    }

    @PostMapping
    private Brand create(
            @RequestBody Brand brand
    ){
        return brandRepo.save(brand);
    }


   /* @PutMapping("{id}")
    public Brand update(
            @PathVariable String id,
            @RequestBody Brand brand
    ){
        Brand brandFromDb = brandRepo.findById(Long.valueOf(id)).get();

        BeanUtils.copyProperties (brand, brandFromDb, "id");    //утила спринга которая копирует все поля из message в messageFromDb кроме id
        return brandRepo.save (brandFromDb);
    }*/

    @PutMapping("{id}")
    public Brand update(
            @PathVariable String id,
            @RequestBody Brand brand
    ){
        Brand brandFromDb = brandRepo.findById(Long.valueOf(id)).get();
//
//        BeanUtils.copyProperties (brand, brandFromDb, "id");
//        return brandRepo.save (brandFromDb);
        return brand;
    }




    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        brandRepo.deleteById(Long.valueOf(id));
    }



}
