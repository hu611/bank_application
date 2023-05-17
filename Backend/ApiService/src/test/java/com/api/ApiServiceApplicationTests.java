package com.api;

import com.api.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiServiceApplicationTests {
    @Autowired
    ApiService apiService;

    @Test
    void contextLoads() {
    }

    @Test
    void testGenerateApiKey() {
        System.out.println(apiService.generateApiKey());
    }

    @Test
    void testKafkaFeign() {
        apiService.send_api_request("prcId","a","b","c","d");
    }

}
