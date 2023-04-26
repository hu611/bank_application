package com.producer.mapper;

import com.producer.pojo.KafkaOffset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-24
 */
@Mapper
public interface KafkaOffsetMapper extends BaseMapper<KafkaOffset> {
    public void updateByPartitionAndTopic(KafkaOffset kafkaOffset);


}
