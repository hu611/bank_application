package com.producer.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.RestResponse;
import com.producer.mapper.KafkaOffsetMapper;
import com.producer.pojo.KafkaOffset;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Duration;
import java.util.*;

import static com.producer.config.KafkaConfig.bootstrapServers;

@Controller
public class ReceiveController {


    @Autowired
    KafkaOffsetMapper kafkaOffsetMapper;

    @Autowired
    KafkaConsumer kafkaConsumer;

    @GetMapping("/receivemsg")
    @ResponseBody
    public List<Object> receiveMsg(@RequestParam("topic") String topic,
                                   @RequestParam("partition") int partition) {


        System.out.println("start to receive message");
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        long offset;
        List<Object> stringList;
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        // 获取当前分区的提交偏移量
        offset = kafkaConsumer.position(topicPartition);
        System.out.println(offset);
        kafkaConsumer.seek(topicPartition,offset);
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
        stringList = new ArrayList<>();
        for (ConsumerRecord consumerRecord : records) {
            stringList.add(consumerRecord.value());
            kafkaConsumer.commitAsync();
        }

        //Create a new thread and update the latest offset to database
        offset += records.count();
        KafkaOffset kafkaOffset = new KafkaOffset();
        kafkaOffset.setOffset(offset);
        kafkaOffset.setTopic(topic);
        kafkaOffset.setPartitionId(partition);
        Runnable updateRunnable = new UpdateThread(kafkaOffset);
        Thread updateThread = new Thread(updateRunnable);
        updateThread.start();
        System.out.println(stringList.size());
        return stringList;
    }

    @GetMapping("/test")
    @ResponseBody
    public void test() {
        System.out.println("Hello, this is a test");
    }

    class UpdateThread implements Runnable {


        KafkaOffset kafkaOffset;

        public UpdateThread(KafkaOffset kafkaOffset) {
            this.kafkaOffset = kafkaOffset;
        }

        @Override
        public void run() {
            kafkaOffsetMapper.updateByPartitionAndTopic(kafkaOffset);
        }
    }
}


