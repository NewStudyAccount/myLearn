// src/main/java/com/example/demo/repository/ProductRepository.java
package com.example.repository;

import com.example.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//方式 1️⃣：使用 MongoRepository（适合简单 CRUD）
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategory(String category);
    List<Product> findByPriceGreaterThan(double price);
}