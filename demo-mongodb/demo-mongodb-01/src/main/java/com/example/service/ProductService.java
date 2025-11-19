// src/main/java/com/example/demo/service/ProductService.java
package com.example.service;

import com.example.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;


//方式 2️⃣：使用 MongoTemplate（适合复杂查询、聚合、更新等）
@Service
public class ProductService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 自定义查询：价格 > 100 且分类为 "Electronics"
    public List<Product> findExpensiveElectronics() {
        Query query = new Query();
        query.addCriteria(Criteria.where("price").gt(100));
        query.addCriteria(Criteria.where("category").is("Electronics"));
        return mongoTemplate.find(query, Product.class);
    }

    // 更新单个文档
    public void updateProductName(String id, String newName) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("name", newName);
        mongoTemplate.updateFirst(query, update, Product.class);
    }

    // 聚合示例：按分类统计数量
    // （可选，如需可用 Aggregation 类实现）


    public void add(){


        Product insert = mongoTemplate.insert(new Product( "Phone", 500, "Electronics"));

    }
}