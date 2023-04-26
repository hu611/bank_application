package com.producer;

import com.producer.mapper.KafkaOffsetMapper;
import com.producer.pojo.KafkaOffset;
import com.producer.service.InitKafkaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

@SpringBootTest
public class InitKafkaDatabase {


    @Autowired
    InitKafkaService initKafkaService;

    @Autowired
    KafkaOffsetMapper kafkaOffsetMapper;

    @Test
    public void InitDatabase() {
        String topic = Constants.creditService_topic;
        int partition = Constants.creditService_partition;
        initKafkaService.InitDatabase(topic, partition);
    }

    @Test
    public void testKafkaMapper() {
        KafkaOffset kafkaOffset = new KafkaOffset();
        kafkaOffset.setOffset(2L);
        kafkaOffset.setPartitionId(0);
        kafkaOffset.setTopic("CreditService");
        kafkaOffsetMapper.updateByPartitionAndTopic(kafkaOffset);
    }

}
