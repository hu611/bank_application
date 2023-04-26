package com.credit.pojo;

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
 * @since 2023-04-25
 */
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

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }
    public LocalDate getOweDate() {
        return oweDate;
    }

    public void setOweDate(LocalDate oweDate) {
        this.oweDate = oweDate;
    }
    public BigDecimal getOweAmount() {
        return oweAmount;
    }

    public void setOweAmount(BigDecimal oweAmount) {
        this.oweAmount = oweAmount;
    }
    public BigDecimal getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(BigDecimal billTotal) {
        this.billTotal = billTotal;
    }
    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }
    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "LoanBill{" +
            "loanId=" + loanId +
            ", oweDate=" + oweDate +
            ", oweAmount=" + oweAmount +
            ", billTotal=" + billTotal +
            ", prcId=" + prcId +
            ", billName=" + billName +
            ", paid=" + paid +
        "}";
    }
}
