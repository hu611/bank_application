package com.baomidou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel(value = "Loan对象", description = "")
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "loan_id", type = IdType.AUTO)
    private Integer loanId;

    @ApiModelProperty("STUDENT LOAN = 0, Car loan = 1, house loan = 2")
    private String loanType;

    private String prcId;

    private BigDecimal paybackTotal;

    private BigDecimal monthlyPayback;

    private BigDecimal interestRate;

    @ApiModelProperty("how much does the person currently owe")
    private BigDecimal currentOwe;

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }
    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
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
        "}";
    }
}
