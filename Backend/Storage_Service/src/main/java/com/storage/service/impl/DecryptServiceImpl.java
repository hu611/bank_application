package com.storage.service.impl;

import com.storage.service.DecryptService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class DecryptServiceImpl implements DecryptService {

    @Override
    public String aes_decrypt(String msg) throws Exception {
        String ALGORITHM = "AES/CBC/PKCS5Padding";
        String SECRET_KEY = "0123456789abcdef0123456789abcdef"; // 密钥
        String IV = "0123456789abcdef"; // 初始化向量
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
        System.out.println(secretKey.toString());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(msg));
        return new String(decrypted);
    }
}
