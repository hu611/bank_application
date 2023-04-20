package com.storage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-09
 */
@TableName("bank_deposit")
public class BankDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "transaction_id", type = IdType.AUTO)
    private Integer transactionId;

    private String cardNo;

    private LocalDate transactionDate;

    private BigDecimal transactionAmount;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public String toString() {
        return "BankDeposit{" +
            "transactionId=" + transactionId +
            ", cardNo=" + cardNo +
            ", transactionDate=" + transactionDate +
            ", transactionAmount=" + transactionAmount +
        "}";
    }
}
