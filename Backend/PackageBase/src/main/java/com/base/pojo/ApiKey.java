package com.base.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-05-16
 */
@Data
@ToString
@AllArgsConstructor
@TableName("api_key")
@ApiModel(value = "ApiKey对象", description = "")
public class ApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String prcId;

    private String apiKey;

    private LocalDate lastUpdate;

    private String accountNum;

}
