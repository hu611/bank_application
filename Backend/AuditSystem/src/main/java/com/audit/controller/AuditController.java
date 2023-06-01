package com.audit.controller;

import com.audit.dto.CreditAuditResponse;
import com.audit.mapper.AuditCreditMapper;
import com.audit.service.AuditService;
import com.base.RestResponse;
import com.base.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class AuditController {

    @Autowired
    AuditService auditService;

    @GetMapping("/getCreditAudit")
    @ResponseBody
    public List<CreditAuditResponse> getCreditAudit() {
        return auditService.getCreditAudit();
    }

    @GetMapping("/getImageUrls/{folderName}/{prcId}")
    @ResponseBody
    public List<String> getImageUrls(@PathVariable String folderName, @PathVariable String prcId) {
        Path folderPath = Paths.get("Images/" + folderName + "/" + prcId);
        return FileUtils.imageNameUnderFolder(folderPath.toString());
    }

    @GetMapping("/image/{prcId}/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String prcId, @PathVariable String filename) {
        try {
            // 读取图片文件并创建字节数组
            Path imagePath = Paths.get("Images/CreditAudit/" + prcId + "/" + filename);
            byte[] imageBytes = Files.readAllBytes(imagePath);

            if (imageBytes.length > 0) {
                // 设置响应头
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);

                // 返回图片给前端
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(imageBytes);
            } else {
                // 图片不存在的情况下返回 404
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常的情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
