package com.base.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static JsonNode _string_to_json(String raw_string) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(raw_string);

    }
}
