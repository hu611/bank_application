package com.storage.service.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.storage.Constants;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Random;

public class UsefulUtils {
    public static String _get_redis_confirm_code_key(String prcId) {
        return prcId + "_confirm_code";
    }

    public static String _get_redis_bank_account_key(int id, String username) {
        return username + "_bank_account_" + id;
    }

    public static String _get_redis_debit_plan_key(int id) {
        return Constants.redis_debit_plan_key + "_" + id;
    }

    public static String _get_redis_debit_balance_key(String cardNo) {
        return "debit_balance_" + cardNo;
    }

    public static String _get_redis_prcid_bankNo_key(String prcId) {
        return "prcid_bankNo_" + prcId;
    }

    public static String _generate_random_num(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static String _get_redis_open_account_code_key(String prcId) {return prcId + "_open_account_code";}

    public static String remove_first_and_last_char(String string) {
        return string.substring(1,string.length()-1);
    }

    /**
     * Get json value string by its field name.
     * @param jsonNode
     * @param fieldName
     * @return null if json node does not have the field.
     * @throws Exception
     */
    public static String get_json_string_by_field(JsonNode jsonNode, String fieldName) throws Exception{
        Object ret = jsonNode.get(fieldName);

        //json does not have the field
        if(ret == null) {
            throw new RuntimeException("Json does not have " + fieldName);
        }

        return remove_first_and_last_char(ret.toString());
    }

}
