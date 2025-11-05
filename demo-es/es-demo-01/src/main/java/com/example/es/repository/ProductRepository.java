package com.example.es.repository;

import com.example.es.domain.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    
    // 自动实现 CRUD + 简单查询
    
    // 方法命名查询：查找 category 精确匹配的商品
    Iterable<Product> findByCategory(String category);
    
    // 全文搜索 name
    Iterable<Product> findByName(String name);
}