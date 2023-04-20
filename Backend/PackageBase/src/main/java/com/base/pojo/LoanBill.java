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
@TableName("loan_bill")
@ApiModel(value = "LoanBill对象", description = "")
public class LoanBill implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer loanId;

    private LocalDate oweDate;

    private BigDecimal oweAmount;

    private BigDecimal billTotal;

    private String prcId;

    private String billName;

    @ApiModelProperty("1: paid 2: unpaid")
    private Integer paid;

}
