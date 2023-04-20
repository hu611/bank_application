package com.baomidou.entity;

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
 * @since 2023-04-17
 */
@TableName("loan_date")
@ApiModel(value = "LoanDate对象", description = "")
public class LoanDate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer loanId;

    private LocalDate loanStartDate;

    private LocalDate loanEndDate;

    @ApiModelProperty("免息时间")
    private LocalDate interestFreeDate;

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }
    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(LocalDate loanStartDate) {
        this.loanStartDate = loanStartDate;
    }
    public LocalDate getLoanEndDate() {
        return loanEndDate;
    }

    public void setLoanEndDate(LocalDate loanEndDate) {
        this.loanEndDate = loanEndDate;
    }
    public LocalDate getInterestFreeDate() {
        return interestFreeDate;
    }

    public void setInterestFreeDate(LocalDate interestFreeDate) {
        this.interestFreeDate = interestFreeDate;
    }

    @Override
    public String toString() {
        return "LoanDate{" +
            "loanId=" + loanId +
            ", loanStartDate=" + loanStartDate +
            ", loanEndDate=" + loanEndDate +
            ", interestFreeDate=" + interestFreeDate +
        "}";
    }
}
