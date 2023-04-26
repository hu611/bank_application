package com.producer;

import org.apache.kafka.common.serialization.StringSerializer;
import com.producer.config.KafkaConfig;
import org.apache.kafka.clients.producer.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

@SpringBootTest
class KafkaProducerApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void testProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        kafkaProducer.send(new ProducerRecord<>("transaction", 0,"key","hello"), new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.println(recordMetadata.topic()+" "+recordMetadata.partition());
            }
        });
        kafkaProducer.close();
    }

    @Test
    void testConsumer() {

    }

}
