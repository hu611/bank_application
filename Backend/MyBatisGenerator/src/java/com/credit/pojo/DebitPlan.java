package com.credit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-05-10
 */
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

    private Integer durationYear;

    private LocalDate startDate;

    private LocalDate expireDate;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }
    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }
    public String getPlanDesc() {
        return planDesc;
    }

    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }
    public Integer getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Integer freezeAmount) {
        this.freezeAmount = freezeAmount;
    }
    public Float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Float interestRate) {
        this.interestRate = interestRate;
    }
    public Integer getDurationMonth() {
        return durationMonth;
    }

    public void setDurationMonth(Integer durationMonth) {
        this.durationMonth = durationMonth;
    }
    public Integer getDurationYear() {
        return durationYear;
    }

    public void setDurationYear(Integer durationYear) {
        this.durationYear = durationYear;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "DebitPlan{" +
            "planId=" + planId +
            ", planTitle=" + planTitle +
            ", planDesc=" + planDesc +
            ", freezeAmount=" + freezeAmount +
            ", interestRate=" + interestRate +
            ", durationMonth=" + durationMonth +
            ", durationYear=" + durationYear +
            ", startDate=" + startDate +
            ", expireDate=" + expireDate +
        "}";
    }
}
