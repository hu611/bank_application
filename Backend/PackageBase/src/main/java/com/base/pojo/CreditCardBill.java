package com.base.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("credit_card_bill")
@ApiModel(value = "CreditCardBill对象", description = "")
public class CreditCardBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "bill_id", type = IdType.AUTO)
    private Integer billId;

    private LocalDate oweDate;

    private BigDecimal oweAmount;

    private BigDecimal billTotal;

    private String prcId;

    private String billName;

    @ApiModelProperty("1: paid 2: unpaid")
    private Integer paid;

}
