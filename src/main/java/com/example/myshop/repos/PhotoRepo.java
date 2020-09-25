package com.example.myshop.repos;

import com.example.myshop.domain.Photo;
import com.example.myshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepo extends JpaRepository<Photo, Long> {

}
