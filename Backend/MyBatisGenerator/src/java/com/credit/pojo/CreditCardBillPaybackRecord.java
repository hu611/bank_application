package com.credit.pojo;

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
 * @since 2023-04-25
 */
@TableName("credit_card_bill_payback_record")
@ApiModel(value = "CreditCardBillPaybackRecord对象", description = "")
public class CreditCardBillPaybackRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    private Integer billId;

    private BigDecimal paybackAmount;

    private LocalDate paybackDate;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }
    public BigDecimal getPaybackAmount() {
        return paybackAmount;
    }

    public void setPaybackAmount(BigDecimal paybackAmount) {
        this.paybackAmount = paybackAmount;
    }
    public LocalDate getPaybackDate() {
        return paybackDate;
    }

    public void setPaybackDate(LocalDate paybackDate) {
        this.paybackDate = paybackDate;
    }

    @Override
    public String toString() {
        return "CreditCardBillPaybackRecord{" +
            "recordId=" + recordId +
            ", billId=" + billId +
            ", paybackAmount=" + paybackAmount +
            ", paybackDate=" + paybackDate +
        "}";
    }
}
