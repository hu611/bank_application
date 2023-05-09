package com.storage.service.feign;

import com.storage.Dto.ProduceMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "KafkaService", fallbackFactory = KafkaFallBackFactory.class, configuration = AuthConfiguration.class)
@RequestMapping("/producer")
public interface KafkaFeign {
    @PostMapping("/sendmsg")
    public String sendMessage(ProduceMessageDto produceMessageDto);
}
