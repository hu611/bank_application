package com.producer.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.producer.mapper.KafkaOffsetMapper;
import com.producer.pojo.KafkaOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KafkaDatabaseController {
    @Autowired
    KafkaOffsetMapper kafkaOffsetMapper;

    @RequestMapping("/getOffset")
    @ResponseBody
    public long getOffset(@RequestParam("topic") String topic, @RequestParam("partition") int partition_id) {
        LambdaQueryWrapper<KafkaOffset> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(KafkaOffset::getPartitionId, partition_id);
        lambdaQueryWrapper.eq(KafkaOffset::getTopic, topic);
        return kafkaOffsetMapper.selectOne(lambdaQueryWrapper).getOffset();
    }
}
