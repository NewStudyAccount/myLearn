package com.example.es.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "product") // 索引名
public class Product {

    @Id
    private String id;

    @Field(type = FieldType.Text) // 全文搜索
    private String name;

    @Field(type = FieldType.Keyword) // 精确匹配/聚合
    private String category;

    @Field(type = FieldType.Double)
    private Double price;

    // 构造函数、Getter、Setter
    public Product() {}
    public Product(String id, String name, String category, Double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }
    // ... getters and setters
}