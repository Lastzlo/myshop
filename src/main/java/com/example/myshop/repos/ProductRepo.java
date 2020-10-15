package com.example.myshop.repos;

import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
