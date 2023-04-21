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
 * @since 2023-04-21
 */
@TableName("credit_card_checkpoint")
@ApiModel(value = "CreditCardCheckpoint对象", description = "")
public class CreditCardCheckpoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "checkpoint_id", type = IdType.AUTO)
    private Integer checkpointId;

    private String cardNo;

    private String prcId;

    private LocalDate checkpointDate;

    private BigDecimal balance;

    private BigDecimal quota;

    public Integer getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Integer checkpointId) {
        this.checkpointId = checkpointId;
    }
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
    public LocalDate getCheckpointDate() {
        return checkpointDate;
    }

    public void setCheckpointDate(LocalDate checkpointDate) {
        this.checkpointDate = checkpointDate;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    @Override
    public String toString() {
        return "CreditCardCheckpoint{" +
            "checkpointId=" + checkpointId +
            ", cardNo=" + cardNo +
            ", prcId=" + prcId +
            ", checkpointDate=" + checkpointDate +
            ", balance=" + balance +
            ", quota=" + quota +
        "}";
    }
}
