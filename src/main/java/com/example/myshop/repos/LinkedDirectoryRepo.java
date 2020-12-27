package com.example.myshop.repos;

import com.example.myshop.domain.LinkedDirectory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedDirectoryRepo extends JpaRepository<LinkedDirectory, Long> {
    LinkedDirectory findByDirectoryType (String directoryType);
}
