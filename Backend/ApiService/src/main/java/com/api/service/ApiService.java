package com.api.service;

public interface ApiService {
    public String generateApiKey();
    public void send_api_request(String debitCardInfo, String companyName, String companyDesc, String fileLoc);
    public void update_api_key(String prcId, int record_id);
}
