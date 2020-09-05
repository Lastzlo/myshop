package com.example.myshop.controller;

import com.example.myshop.domain.Brand;
import com.example.myshop.repos.BrandRepo;
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


    @PutMapping("{id}")
    public Brand update(
            @PathVariable String id,
            @RequestBody Brand brand
    ){
        Brand brandfromDb = brandRepo.findById(Long.valueOf(id)).get();

        BeanUtils.copyProperties (brand, brandfromDb, "id");    //утила спринга которая копирует все поля из message в messageFromDb кроме id
        Brand updatedBrand = brandRepo.save (brandfromDb);
        return updatedBrand;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        brandRepo.deleteById(Long.valueOf(id));
    }



}
