package com.example.controller;

import com.example.es.repository.UserElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private UserElasticsearchRepository productRepository;

    // 保存商品
//    @PostMapping
//    public Product save(@RequestBody Product product) {
//        return productRepository.save(product);
//    }

    // 全文搜索
//    @GetMapping("/search/name")
//    public Iterable<Product> searchByName(@RequestParam String name) {
//        return productRepository.findByName(name);
//    }

    // 精确分类查询
//    @GetMapping("/search/category")
//    public Iterable<Product> findByCategory(@RequestParam String category) {
//        return productRepository.findByCategory(category);
//    }

    // 获取所有
    @GetMapping("/get")
    public void searchByName() {
         productRepository.searchByName("");
    }


    @GetMapping("/findAll")
    public void findAll() {
        productRepository.findAll();
    }

    @GetMapping("/findAllWithClass")
    public void findWithClass() {
        productRepository.findWithClass();
    }

}