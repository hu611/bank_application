package com.base.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-05-10
 */
@Data
@TableName("debit_plan")
@ApiModel(value = "DebitPlan对象", description = "")
public class DebitPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "plan_id", type = IdType.AUTO)
    private Integer planId;

    private String planTitle;

    private String planDesc;

    @ApiModelProperty("冻结资金一定是整数")
    private Integer freezeAmount;

    private Float interestRate;

    private Integer durationMonth;

    private LocalDate startDate;

    private LocalDate expireDate;
}
