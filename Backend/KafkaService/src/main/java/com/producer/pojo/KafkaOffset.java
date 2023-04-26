package com.producer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-24
 */
@Data
@ToString
@TableName("kafka_offset")
@ApiModel(value = "KafkaOffset对象", description = "")
public class KafkaOffset implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer partitionId;

    private String topic;

    private Long offset;

}
