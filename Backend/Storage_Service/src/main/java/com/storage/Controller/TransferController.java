package com.storage.Controller;

import com.base.RestResponse;
import com.storage.service.DecryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransferController {
    @Autowired
    DecryptService decryptService;

    @PostMapping("/transfer")
    @ResponseBody
    public String transfer_money(@RequestParam("transaction") String encrypted_transaction) {
        try {
            String transaction = decryptService.aes_decrypt(encrypted_transaction);
        } catch (Exception e) {
            RestResponse.validfail("Decyrption step failed, please provide a good thing");
        }
    }
}
