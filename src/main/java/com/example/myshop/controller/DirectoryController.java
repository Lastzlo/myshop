package com.example.myshop.controller;

import com.example.myshop.domain.DirectoryType;
import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Views;
import com.example.myshop.repos.LinkedDirectoryRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("directory")
public class DirectoryController {
    private final LinkedDirectoryRepo directoryRepo;

    @Autowired
    public DirectoryController (LinkedDirectoryRepo directoryRepo) {
        this.directoryRepo = directoryRepo;
    }

    @GetMapping("getCore")
    @JsonView(Views.OnlyChild.class)
    public LinkedDirectory getCore(){
        LinkedDirectory directory = directoryRepo.findByDirectoryType (DirectoryType.CATEGORY_LIST.toString ());
        if (directory==null){
            directory = new LinkedDirectory ("Категории", DirectoryType.CATEGORY_LIST.toString ());
            directory = directoryRepo.save (directory);
        }
        return directory;
    }

    /*@GetMapping("{id}")
    public LinkedDirectory getOne(@PathVariable String id){
        return directoryRepo.getOne(Long.valueOf(id));
    }*/

    @PostMapping
    @JsonView(Views.OnlyChild.class)
    private LinkedDirectory create(
            @RequestBody LinkedDirectory linkedDirectory
    ){
        LinkedDirectory father = directoryRepo.getOne (linkedDirectory.getFather ().getId ());

        if(father!=null){
            linkedDirectory.setFather (father);
            LinkedDirectory child = directoryRepo.save (linkedDirectory);

            father.addChild (child);
            directoryRepo.save (father);

            return child;
        } else {
            return directoryRepo.save (linkedDirectory);
        }
    }

    @PutMapping("{id}")
    @JsonView(Views.OnlyChild.class)
    public LinkedDirectory update(
            @PathVariable String id,
            @RequestBody LinkedDirectory directory
    ){
        if(directoryRepo.findById(Long.valueOf(id)).isPresent ()){
            LinkedDirectory directoryFromDb = directoryRepo.findById(Long.valueOf(id)).get();

            directoryFromDb.setName (directory.getName ());
            return directoryRepo.save (directoryFromDb);
        }
        return directory;
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        directoryRepo.findById (Long.valueOf (id)).ifPresent (
                child -> {
                    if(child.getFather ()!=null){
                        LinkedDirectory father = child.getFather ();

                        father.deleteChild (child);
                        directoryRepo.save (father);
                    }

                    if(child.getChildren ().size () != 0){
                        Set<LinkedDirectory> children = child.getChildren ();

                        children.forEach ((item)-> item.setFather (null));
                        child.getChildren ().clear ();
                        //children.forEach ((item)-> directoryRepo.deleteById(item.getId ()));
                        children.forEach ((item)-> directoryRepo.delete (item));
                    }
                    //directoryRepo.deleteById(Long.valueOf(id));
                    directoryRepo.delete (child);

                }
        );




    }




    /*
    @RestController
@RequestMapping("category")
public class CategoryController {
    private final CategoryRepo categoryRepo;


    @Autowired
    public CategoryController (CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    @JsonView(Views.Brands.class)
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
        Category categoryFromDb = categoryRepo.findById(Long.valueOf(id)).get();

        categoryFromDb.setCategoryName (category.getCategoryName ());

        //BeanUtils.copyProperties (category, categoryFromDb, "id");    //утила спринга которая копирует все поля из message в messageFromDb кроме id
        return categoryRepo.save (categoryFromDb);
    }



    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        categoryRepo.deleteById(Long.valueOf(id));
    }
    */

}
