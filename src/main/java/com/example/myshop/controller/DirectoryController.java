package com.example.myshop.controller;

import com.example.myshop.domain.DirectoryType;
import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Product;
import com.example.myshop.domain.Views;
import com.example.myshop.repos.LinkedDirectoryRepo;
import com.example.myshop.repos.ProductRepo;
import com.example.myshop.services.ProductService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("directory")
public class DirectoryController {
    private final ProductRepo productRepo;

    private final LinkedDirectoryRepo directoryRepo;

    @Autowired
    public DirectoryController (ProductRepo productRepo,  LinkedDirectoryRepo directoryRepo) {
        this.productRepo = productRepo;
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

    @GetMapping("getProductByDirectoryId/{id}")
    @JsonView(Views.FullMessage.class)
    public Set<Product> getProductsByLinkedDirectoryId(@PathVariable String id){
        Optional<LinkedDirectory> optionalLinkedDirectory = directoryRepo.findById (Long.valueOf (id));
        if (optionalLinkedDirectory.isPresent ()){
            return optionalLinkedDirectory.get ().getProducts ();
        } else return new HashSet<> ();

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

        LinkedDirectory child = linkedDirectory;

        if(father!=null){
            String fatherDirectoryType = father.getDirectoryType ();

            child.setFather (father);

            //проверка что отец CATEGORY_LIST
            if(fatherDirectoryType.equals (DirectoryType.CATEGORY_LIST.toString ())){
                child.setDirectoryType (DirectoryType.CATEGORY.toString ());
                child = directoryRepo.save (child);

                //добавляем в него дочернюю директорию Бренды
                LinkedDirectory brandList = new LinkedDirectory (
                        "Бренды",
                        DirectoryType.BRAND_LIST.toString ()
                );
                brandList.setFather (child);
                brandList = directoryRepo.save (brandList);

                //добавляем в него дочернюю директорию Параметры
                LinkedDirectory parameterList = new LinkedDirectory (
                        "Параметры",
                        DirectoryType.PARAMETER_LIST.toString ()
                );
                parameterList.setFather (child);
                parameterList = directoryRepo.save (parameterList);

                child.addChild (brandList);
                child.addChild (parameterList);
            }

            //проверка что отец BRAND_LIST
            if(fatherDirectoryType.equals (DirectoryType.BRAND_LIST.toString ())){

                child.setDirectoryType (DirectoryType.BRAND.toString ());
            }

            //проверка что отец PARAMETER_LIST
            if(fatherDirectoryType.equals (DirectoryType.PARAMETER_LIST.toString ())){

                child.setDirectoryType (DirectoryType.PARAMETER.toString ());
            }

            child = directoryRepo.save (child);

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
                linkedDirectory -> {
                    //удаление тега из всех товаров
                    linkedDirectory.getProducts ().forEach (
                            product -> {
                                product.deleteTag (linkedDirectory);
                                productRepo.save (product);
                            }
                    );

                    //удаление у родителя
                    if(linkedDirectory.getFather ()!=null){
                        LinkedDirectory father = linkedDirectory.getFather ();

                        father.deleteChild (linkedDirectory);
                        directoryRepo.save (father);
                    }

                    //удаление всех детей
                    if(linkedDirectory.getChildren ().size () != 0){
                        Set<LinkedDirectory> children = linkedDirectory.getChildren ();

                        children.forEach (item-> item.setFather (null));
                        linkedDirectory.getChildren ().clear ();

                        children.forEach (item-> {
                            //удаление тега из всех товаров
                            item.getProducts ().forEach (
                                    product -> {
                                        product.deleteTag (item);
                                        productRepo.save (product);
                                    }
                            );

                            directoryRepo.delete (item);
                        });
                    }

                    directoryRepo.delete (linkedDirectory);
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
