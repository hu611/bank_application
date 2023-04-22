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
 * @since 2023-04-22
 */
@TableName("credit_card_bill")
@ApiModel(value = "CreditCardBill对象", description = "")
public class CreditCardBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "bill_id", type = IdType.AUTO)
    private Integer billId;

    private LocalDate oweDate;

    private BigDecimal oweAmount;

    private BigDecimal billTotal;

    private String prcId;

    private String billName;

    @ApiModelProperty("1: paid 2: unpaid")
    private Integer paid;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
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
        return "CreditCardBill{" +
            "billId=" + billId +
            ", oweDate=" + oweDate +
            ", oweAmount=" + oweAmount +
            ", billTotal=" + billTotal +
            ", prcId=" + prcId +
            ", billName=" + billName +
            ", paid=" + paid +
        "}";
    }
}
