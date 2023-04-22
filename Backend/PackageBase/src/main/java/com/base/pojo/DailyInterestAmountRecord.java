package com.base.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
 * @since 2023-04-22
 */
@Data
@ToString
@TableName("daily_interest_amount_record")
@ApiModel(value = "DailyInterestAmountRecord对象", description = "")
public class DailyInterestAmountRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal interestAmount;

    private LocalDate recordDate;

    private String prcId;

}
