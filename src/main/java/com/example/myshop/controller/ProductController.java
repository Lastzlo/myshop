package com.example.myshop.controller;

import com.example.myshop.exeptions.NotFoundExeption;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("product")
public class ProductController {
    private int count = 3;

    private ArrayList<Map<String, String>> products = new ArrayList<> () {{
        add (new HashMap<> () {{
            put ("id", "1");
            put ("name", "Razer mouse");
        }});
        add (new HashMap<> () {{
            put ("id", "2");
            put ("name", "Stilseries mouse");
        }});
    }};

    @GetMapping
    public List<Map<String,String>> list(){
        return products;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id){
        return getProduct (id);
    }

    private Map<String, String> getProduct (String id) {
        return products.stream ()
                .filter (products -> products.get ("id").equals (id))
                .findFirst ()
                .orElseThrow (NotFoundExeption::new);   //обработка случая когда не найдено продукт
    }

    @RequestMapping("create")
    @PostMapping
    private Map<String, String> create(
            @RequestPart("file")MultipartFile file,
            @RequestPart("product") Map<String, String> product
            //@RequestBody Map<String, String> product
            ){
        product.put ("id", String.valueOf (count++));
        product.put ("productPicture", file.getOriginalFilename ());
        products.add (product);

        return product;
    }

    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> product){
        Map<String, String> productFromDb = getProduct (id);

        productFromDb.putAll (product);
        productFromDb.put ("id", id);

        return productFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Map<String, String> product = getProduct (id);

        products.remove (product);
    }



}
