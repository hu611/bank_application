package com.baomidou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-04-20
 */
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

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }
    public Integer getLoanType() {
        return loanType;
    }

    public void setLoanType(Integer loanType) {
        this.loanType = loanType;
    }
    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public BigDecimal getPaybackTotal() {
        return paybackTotal;
    }

    public void setPaybackTotal(BigDecimal paybackTotal) {
        this.paybackTotal = paybackTotal;
    }
    public BigDecimal getMonthlyPayback() {
        return monthlyPayback;
    }

    public void setMonthlyPayback(BigDecimal monthlyPayback) {
        this.monthlyPayback = monthlyPayback;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public BigDecimal getCurrentOwe() {
        return currentOwe;
    }

    public void setCurrentOwe(BigDecimal currentOwe) {
        this.currentOwe = currentOwe;
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

    @Override
    public String toString() {
        return "Loan{" +
            "loanId=" + loanId +
            ", loanType=" + loanType +
            ", prcId=" + prcId +
            ", paybackTotal=" + paybackTotal +
            ", monthlyPayback=" + monthlyPayback +
            ", interestRate=" + interestRate +
            ", currentOwe=" + currentOwe +
            ", loanStartDate=" + loanStartDate +
            ", loanEndDate=" + loanEndDate +
        "}";
    }
}
