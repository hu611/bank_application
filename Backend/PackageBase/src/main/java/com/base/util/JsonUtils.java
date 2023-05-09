package com.base.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static JsonNode _string_to_json(String raw_string) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(raw_string);
    }

    public static String preprocess_jsonnode(String json) {
        //不然就是 比如 amount会变成 "500" 而不是500.
        return json.substring(1,json.length()-1);
    }

    public static JsonNode _object_to_json(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(object);
    }
}
