package com.base.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value = "Loan对象", description = "")
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "loan_id", type = IdType.AUTO)
    private Integer loanId;

    @ApiModelProperty("0:car, 1:house, 2:student")
    private Integer loanType;

    private String prcId;

    private BigDecimal paybackTotal;

    private BigDecimal monthlyPayback;

    private BigDecimal interestRate;

    @ApiModelProperty("how much does the person currently owe")
    private BigDecimal currentOwe;

    private LocalDate loanStartDate;

    private LocalDate loanEndDate;

}
