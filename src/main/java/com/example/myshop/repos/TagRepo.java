package com.example.myshop.repos;

import com.example.myshop.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Long> {
}
