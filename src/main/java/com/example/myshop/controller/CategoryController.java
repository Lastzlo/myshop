package com.example.myshop.controller;

import com.example.myshop.domain.Category;
import com.example.myshop.repos.CategoryRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    private final CategoryRepo categoryRepo;


    @Autowired
    public CategoryController (CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public List<Category> list(){
        return categoryRepo.findAll();
    }

    @GetMapping("{id}")
    public Category getOne(@PathVariable String id){
        return categoryRepo.getOne(Long.valueOf(id));
    }

    @PostMapping
    private Category create(
            @RequestBody Category category
    ){
        return categoryRepo.save(category);
    }


    @PutMapping("{id}")
    public Category update(
            @PathVariable String id,
            @RequestBody Category category
    ){
        Category categoryfromDb = categoryRepo.findById(Long.valueOf(id)).get();

        BeanUtils.copyProperties (category, categoryfromDb, "id");    //утила спринга которая копирует все поля из message в messageFromDb кроме id
        Category updatedCategory = categoryRepo.save (categoryfromDb);
        return updatedCategory;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        categoryRepo.deleteById(Long.valueOf(id));
    }



}
