package com.base.pojo;

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
 * @since 2023-04-21
 */
@TableName("credit_card")
@ApiModel(value = "CreditCard对象", description = "")
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cardNo;

    private String prcId;

    private LocalDate openingDate;

    private BigDecimal balance;

    @ApiModelProperty("预借金额")
    private BigDecimal cashAdvance;

    private BigDecimal quota;

    private LocalDate lastBillDate;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }
    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getCashAdvance() {
        return cashAdvance;
    }

    public void setCashAdvance(BigDecimal cashAdvance) {
        this.cashAdvance = cashAdvance;
    }
    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }
    public LocalDate getLastBillDate() {
        return lastBillDate;
    }

    public void setLastBillDate(LocalDate lastBillDate) {
        this.lastBillDate = lastBillDate;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
            "cardNo=" + cardNo +
            ", prcId=" + prcId +
            ", openingDate=" + openingDate +
            ", balance=" + balance +
            ", cashAdvance=" + cashAdvance +
            ", quota=" + quota +
            ", lastBillDate=" + lastBillDate +
        "}";
    }
}
