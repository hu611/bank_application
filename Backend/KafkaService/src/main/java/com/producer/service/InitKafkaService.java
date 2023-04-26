package com.producer.service;

import com.producer.Constants;
import com.producer.mapper.KafkaOffsetMapper;
import com.producer.pojo.KafkaOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitKafkaService {
    @Autowired
    KafkaOffsetMapper kafkaOffsetMapper;

    @Transactional
    public void InitDatabase(String topic, int partition) {
        for (int i = 0; i < partition; i++) {
            KafkaOffset kafkaOffset = new KafkaOffset();
            kafkaOffset.setOffset(0L);
            kafkaOffset.setTopic(topic);
            kafkaOffset.setPartitionId(i);
            kafkaOffsetMapper.insert(kafkaOffset);
        }
    }

}
