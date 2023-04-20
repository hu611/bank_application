package com.storage.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface DecryptService {

    public JsonNode aes_decrypt(String msg) throws Exception;
}
