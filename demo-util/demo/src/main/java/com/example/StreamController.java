package com.example;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;

@RestController
@RequestMapping("/stream")
public class StreamController {

    /**
     * 返回一个 Flux，每隔 1 秒产生一个数字。
     * 使用 APPLICATION_STREAM_JSON_VALUE 可以让客户端流式地接收 JSON 对象。
     * 注意：在 Spring Boot 2.x 中通常使用 application/stream+json，
     * 但在较新的版本或某些浏览器中，NDJSON (application/x-ndjson) 更为通用。
     */
    @GetMapping(value = "/data", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DataObj> streamData() {



        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> new DataObj("Data-" + sequence, System.currentTimeMillis()))
                .take(10);
    }

    // 简单的数据载体
    static class DataObj {
        public String name;
        public long timestamp;

        public DataObj(String name, long timestamp) {
            this.name = name;
            this.timestamp = timestamp;
        }
    }
}
