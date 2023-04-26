package com.credit.Service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="KafkaService", fallbackFactory = KafkaFeignFallBackFactory.class)
@RequestMapping("/producer")
public interface KafkaFeign {
    @GetMapping("/receivemsg")
    @ResponseBody
    public List<Object> receiveMsg(@RequestParam("topic") String topic,
                                   @RequestParam("partition") int partition);
}
