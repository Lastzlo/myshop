package com.example.myshop.services;

import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Product;

import com.example.myshop.repos.LinkedDirectoryRepo;
import com.example.myshop.repos.ProductRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Set<LinkedDirectory> tags = product.getTags ();

        product.getTags ().clear ();

        tags.forEach (tag -> {
            LinkedDirectory tagFromDb = this.directoryRepo.findById (tag.getId ()).get ();

            product.addTag(tagFromDb);
        });

        return productRepo.save(product);

    }
}
