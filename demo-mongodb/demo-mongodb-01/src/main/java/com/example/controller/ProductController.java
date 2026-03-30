// src/main/java/com/example/demo/controller/ProductController.java
package com.example.controller;

import com.example.domain.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // 1. 使用 Repository 保存
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 2. 使用 Repository 查询
    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }

    // 3. 使用 MongoTemplate 复杂查询
    @GetMapping("/expensive-electronics")
    public List<Product> getExpensiveElectronics() {
        return productService.findExpensiveElectronics();
    }

    // 4. 使用 MongoTemplate 更新
    @PutMapping("/{id}/name")
    public void updateProductName(@PathVariable String id, @RequestParam String name) {
        productService.updateProductName(id, name);
    }


    @PostMapping("/add")
    public void add() {
        productService.add();
    }
}