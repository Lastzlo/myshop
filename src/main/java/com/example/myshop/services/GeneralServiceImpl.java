package com.example.myshop.services;

import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Product;

import com.example.myshop.domain.Views;
import com.example.myshop.repos.LinkedDirectoryRepo;
import com.example.myshop.repos.ProductRepo;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GeneralServiceImpl implements  GeneralService{
    @Autowired
    private LinkedDirectoryRepo directoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> getAllProducts () {
        return productRepo.findAll();
    }

    @Override
    public Product getProduct (Long id) {
        return productRepo.getOne(id);
    }

    @Override
    public Product saveProduct (Product product) {
        product.setCreationDate (LocalDateTime.now ());

        Set<LinkedDirectory> tags = new HashSet<> (){{addAll (product.getTags ());}};
        product.getTags ().clear ();
        tags.forEach (tag -> {
                    this.directoryRepo.findById (tag.getId ()).ifPresent (
                            tagFromDb -> product.addTag (tagFromDb)
                    );
                }
        );

        return productRepo.save(product);
    }

    @Override
    public void deleteProduct (Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public Product updateProduct (Product product) {

        Product productFromDb = productRepo.findById (product.getId ()).get ();

        if(productFromDb!= null){
            BeanUtils.copyProperties (product, productFromDb, "id", "tags", "photos", "creationDate");
            productFromDb.setTags (product.getTags ());
            return productRepo.save (productFromDb);
        }

        return product;
    }
}
