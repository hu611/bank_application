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
 * @since 2023-04-20
 */

@Data
@ToString
@TableName("credit_card")
@ApiModel(value = "CreditCard对象", description = "")
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cardNo;

    private String prcId;

    private LocalDate openingDate;

    private BigDecimal balance;

    @ApiModelProperty("预借金额")
    private BigDecimal cashAdvance;

    private BigDecimal quota;

}
