package com.base.pojo;

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
 * @since 2023-04-21
 */
@TableName("transaction_record")
@ApiModel(value = "TransactionRecord对象", description = "")
public class TransactionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "transaction_id", type = IdType.AUTO)
    private Integer transactionId;

    private String encodedTransaction;

    private LocalDate transactionDate;

    private String transactionType;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }
    public String getEncodedTransaction() {
        return encodedTransaction;
    }

    public void setEncodedTransaction(String encodedTransaction) {
        this.encodedTransaction = encodedTransaction;
    }
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
            "transactionId=" + transactionId +
            ", encodedTransaction=" + encodedTransaction +
            ", transactionDate=" + transactionDate +
            ", transactionType=" + transactionType +
        "}";
    }
}
