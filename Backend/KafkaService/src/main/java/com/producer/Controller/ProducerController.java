package com.producer.Controller;

import com.producer.dto.ProduceMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class ProducerController {
    @Autowired
    KafkaProducer kafkaProducer;

    @PostMapping("/sendmsg")
    @ResponseBody
    public String sendMessage(@RequestBody ProduceMessageDto produceMessageDto) {
        List<Integer> partitionList = produceMessageDto.getPartition();
        String topic = produceMessageDto.getTopic();
        String key = produceMessageDto.getKey();
        String value = produceMessageDto.getValue();
        for(int partition: partitionList) {
            ProducerRecord producerRecord = new ProducerRecord<>(topic,
                    key,value);
            try {
                kafkaProducer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        log.info("success: " + metadata.topic() + metadata.partition());
                    }

                });
            } catch (Exception e) {
                return "Unsuccessful";
            }
        }
        return "successful";


    }
}
