package com.example.myshop.services;

import com.example.myshop.domain.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface GeneralService {
    List<Product> getAllProducts ();

    Product getProduct (Long id);

    Product saveProduct (Product product);

    Product saveProductWithFile (Product product, Optional<MultipartFile[]> files);

    void deleteProduct (Long valueOf);

    Product updateProduct (Product product);

    Product updateProductWithFile (Product product, Optional<MultipartFile[]> files);
}
