package com.example.myshop.services;

import com.example.myshop.domain.Product;

import java.util.List;

public interface GeneralService {
    List<Product> getAllProducts ();

    Product getProduct (Long id);

    Product saveProduct (Product product);
}
