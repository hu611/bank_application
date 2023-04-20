package com.producer.Controller;

import com.base.RestResponse;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Controller
public class ReceiveController {
    @Autowired
    KafkaConsumer kafkaConsumer;

    @GetMapping("/receivemsg")
    @ResponseBody
    public RestResponse receiveMsg(String topic, int partition, int offset) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        Collection<TopicPartition> topicPartitionCollection = new ArrayList<>();
        topicPartitionCollection.add(topicPartition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        kafkaConsumer.seek(topicPartition,offset);
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
        kafkaConsumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset)));
        return RestResponse.success(records);
    }
}
