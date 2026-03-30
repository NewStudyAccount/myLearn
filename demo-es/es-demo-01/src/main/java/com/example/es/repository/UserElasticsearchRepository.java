package com.example.es.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.example.es.domain.SysMenu;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserElasticsearchRepository {

    private final ElasticsearchClient client;

    public UserElasticsearchRepository(ElasticsearchClient client) {
        this.client = client;
    }


    // 全文搜索
    public void searchByName(String name)  {
//        SearchResponse<User> response = client.search(s -> s
//            .index("users")
//            .query(q -> q
//                .match(t -> t
//                    .field("name")
//                    .query(name)
//                )
//            ),
//            User.class
//        );
//        return response.hits().hits().stream()
//            .map(Hit::source)
//            .collect(Collectors.toList());

        SearchRequest searchRequest = SearchRequest.of(s -> s
            .index("default_index")
            .query(q -> q
                .match(t -> t
                    .field("name")
                    .query(name)
                )
            )
        );
        try {
            SearchResponse<Void> search = client.search(searchRequest);
            System.out.println(search);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    // ✅ 查询全部数据（不推荐用于大数据量！）
    public void findAll() {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("default_index")          // 替换为你的索引名
                .query(q -> q.matchAll(m -> m))  // match_all 查询
                .size(10000)             // ES 默认最多返回 10,000 条（受 max_result_window 限制）
        );

        SearchResponse<JsonData> response = null;
        try {
            response = client.search(searchRequest, JsonData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response);


        // 打印原始 JSON
        for (Hit<JsonData> hit : response.hits().hits()) {
            System.out.println(hit.source().toJson()); // 输出 JSON 字符串
        }

//        return response.hi//
    }

    public void findWithClass(){

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("default_index")          // 替换为你的索引名
                .query(q -> q.matchAll(m -> m))  // match_all 查询
                .size(10000)             // ES 默认最多返回 10,000 条（受 max_result_window 限制）
        );


        // 假设你有 User 类（带无参构造、@JsonIgnoreProperties 等）
        SearchResponse<SysMenu> response = null;
        try {
            response = client.search(searchRequest, SysMenu.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Hit<SysMenu> hit : response.hits().hits()) {
            System.out.println(hit.source()); // User 对象
        }
    }

}