package com.example.myshop.repos;

import com.example.myshop.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepo extends JpaRepository<Brand, Long> {

}
