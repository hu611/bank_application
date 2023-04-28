package com.storage;

import com.base.util.DecryptUtils;
import com.base.util.EmailUtils;
import com.base.util.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.storage.Dto.ProduceMessageDto;
import com.storage.service.feign.KafkaFeign;
import io.swagger.models.auth.In;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.*;

@SpringBootTest
class StorageServiceApplicationTests {
    @Autowired
    KafkaFeign kafkaFeign;

    @Test
    void contextLoads() {
        String a = "3031323334353637383961626364656630313233343536373839616263646566";
        System.out.println(a.length());
    }

    @Test
    void testConsumer() {
        String topic = "transaction";
        int partition = 0;
        String bootstrap_server = "localhost:9092";
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap_server);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        Collection<TopicPartition> topicCollection = new ArrayList<>();
        topicCollection.add(topicPartition);
        kafkaConsumer.assign(Collections.singletonList(topicPartition));
        kafkaConsumer.seek(topicPartition,2);
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
        }
        kafkaConsumer.commitSync();
    }

    @Test
    public void test_aes_decryption() {
        try {
            JsonNode jsonNode = DecryptUtils.aes_decrypt(
                    "+9MAKhQ75eno1GkLxf8K7u+3R9V+8j8RNZ2UeZM82GbBRuEeA0OiN5DompKZWUlXaYoexujNoERyyOCb7af0vg==");
            System.out.println(jsonNode.get("account"));
            System.out.println(jsonNode.get("confirmCode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_email_send() {
        try {
            EmailUtils._send_email("AustinHu0802@gmail.com", "Hello from Java", "llll");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_create_folder() {
        String folderPath = "../Images/CreditAudit/test"; // Replace with your desired folder path
        System.out.println(FileUtils.create_folder(folderPath));
    }

    @Test
    public void test_kafka_feign() {
        ProduceMessageDto produceMessageDto = new ProduceMessageDto();
        List<Integer> al = new ArrayList<Integer>();
        al.add(0);
        produceMessageDto.setPartition(al);
        produceMessageDto.setTopic("CreditService");
        produceMessageDto.setValue("hello");
        kafkaFeign.sendMessage(produceMessageDto);
    }




}
