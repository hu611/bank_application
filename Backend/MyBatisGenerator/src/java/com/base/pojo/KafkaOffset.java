package com.base.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-24
 */
@TableName("kafka_offset")
@ApiModel(value = "KafkaOffset对象", description = "")
public class KafkaOffset implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer partitionId;

    private String topic;

    private Integer offset;

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "KafkaOffset{" +
            "partitionId=" + partitionId +
            ", topic=" + topic +
            ", offset=" + offset +
        "}";
    }
}
