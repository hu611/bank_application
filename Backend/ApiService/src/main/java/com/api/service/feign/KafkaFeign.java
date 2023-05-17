package com.api.service.feign;

import com.api.Dto.ProduceMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="KafkaService")
@RequestMapping("/producer")
public interface KafkaFeign {
    @GetMapping("/sendmsg")
    @ResponseBody
    public String sendMessage(@RequestBody ProduceMessageDto produceMessageDto);
}