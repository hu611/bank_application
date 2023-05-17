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
        String topic = Constants.apiService_topic;
        int partition = Constants.apiService_partition;
        initKafkaService.InitDatabase(topic, partition);
    }

    @Test
    public void testKafkaMapper() {
        KafkaOffset kafkaOffset = new KafkaOffset();
        kafkaOffset.setOffset(0L);
        kafkaOffset.setPartitionId(0);
        kafkaOffset.setTopic("ApiService");
        kafkaOffsetMapper.updateByPartitionAndTopic(kafkaOffset);
    }

}
