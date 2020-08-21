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

    @RequestMapping("add")
    @PostMapping
    private void add(
            @RequestPart("file")MultipartFile file,
            @RequestPart("product") Map<String, String> product
            ){
        product.put ("id", String.valueOf (count++));
        product.put ("ProductPicture", file.getOriginalFilename ());

        products.add (product);
    }

    /*
    fetch(
  '/product/getOne',
  {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id: 1 })
  }).then(result => result.json().then(console.log))
    * */

    @PostMapping("getOne")
    public Map<String, String> getOne(@RequestBody Map<String, String> product ){
        return getMessage (product.get ("id"));       //обработка случая когда не найдено сообщение
    }

    private Map<String, String> getMessage (String id) {
        return products.stream ()
                .filter (products -> products.get ("id").equals (id))
                .findFirst ()
                .orElseThrow (NotFoundExeption::new);
    }



    @GetMapping
    private List<Map<String,String>> list(){
        return products;
    }

}
