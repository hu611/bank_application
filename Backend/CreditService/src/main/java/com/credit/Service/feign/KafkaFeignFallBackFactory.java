package com.credit.Service.feign;

import feign.hystrix.FallbackFactory;

import java.util.List;

public class KafkaFeignFallBackFactory implements FallbackFactory<KafkaFeign> {
    @Override
    public KafkaFeign create(Throwable throwable) {
        return new KafkaFeign() {
            @Override
            public List<Object> receiveMsg(String topic, int partition) {
                return null;
            }
        };
    }
}
