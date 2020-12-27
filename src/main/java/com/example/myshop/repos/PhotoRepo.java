package com.example.myshop.repos;

import com.example.myshop.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepo extends JpaRepository<Photo, Long> {

}
