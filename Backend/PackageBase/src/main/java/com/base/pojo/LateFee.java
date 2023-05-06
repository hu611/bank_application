package com.base.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-05-06
 */
@TableName("late_fee")
@ApiModel(value = "LateFee对象", description = "")
public class LateFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "late_fee_id", type = IdType.AUTO)
    private Integer lateFeeId;

    private String prcId;

    private LocalDate lateDate;

    private BigDecimal lateFeeAmount;

    public Integer getLateFeeId() {
        return lateFeeId;
    }

    public void setLateFeeId(Integer lateFeeId) {
        this.lateFeeId = lateFeeId;
    }
    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public LocalDate getLateDate() {
        return lateDate;
    }

    public void setLateDate(LocalDate lateDate) {
        this.lateDate = lateDate;
    }
    public BigDecimal getLateFeeAmount() {
        return lateFeeAmount;
    }

    public void setLateFeeAmount(BigDecimal lateFeeAmount) {
        this.lateFeeAmount = lateFeeAmount;
    }

    @Override
    public String toString() {
        return "LateFee{" +
            "lateFeeId=" + lateFeeId +
            ", prcId=" + prcId +
            ", lateDate=" + lateDate +
            ", lateFeeAmount=" + lateFeeAmount +
        "}";
    }
}
