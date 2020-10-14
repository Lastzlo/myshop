package com.example.myshop.repos;

import com.example.myshop.domain.LinkedDirectory;
import com.example.myshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface LinkedDirectoryRepo extends JpaRepository<LinkedDirectory, Long> {
    LinkedDirectory findByDirectoryType (String directoryType);

}
