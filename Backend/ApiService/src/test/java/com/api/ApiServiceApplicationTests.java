package com.api;

import com.api.Dto.PayTerm;
import com.api.service.ApiService;
import com.base.util.DecryptUtils;
import com.base.util.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
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

    @Test
    void testTransfer() {
        PayTerm payTerm = new PayTerm("200","123456789","1932","abc123",0);
        JsonNode jsonNode = JsonUtils._object_to_json(payTerm);
        try {
            String aesString = DecryptUtils.aes_encrypt(jsonNode);
            apiService.pay_with_api_key(aesString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
