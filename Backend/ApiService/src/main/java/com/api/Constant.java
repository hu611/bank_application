package com.api;

public class Constant {
    public static final int API_KEY_LENGTH = 7;
    public final static String apiService_topic = "ApiService";
    public final static int apiService_partition = 2;

    public static String getApiKeyRedis(String prcId) {
        return prcId + "_apiKey";
    }

    public static String getAccountNumRedis(String apiKey) {
        return apiKey + "_accountNumKey";
    }
}
