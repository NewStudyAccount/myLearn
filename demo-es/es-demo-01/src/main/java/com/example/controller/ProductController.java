package com.example.controller;

import com.example.es.domain.Product;
import com.example.es.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 保存商品
    @PostMapping
    public Product save(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 全文搜索
    @GetMapping("/search/name")
    public Iterable<Product> searchByName(@RequestParam String name) {
        return productRepository.findByName(name);
    }

    // 精确分类查询
    @GetMapping("/search/category")
    public Iterable<Product> findByCategory(@RequestParam String category) {
        return productRepository.findByCategory(category);
    }

    // 获取所有
    @GetMapping
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }
}