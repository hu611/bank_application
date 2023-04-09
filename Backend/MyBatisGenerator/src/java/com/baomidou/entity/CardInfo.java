package com.baomidou.entity;

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
 * @since 2023-04-09
 */
@TableName("card_info")
@ApiModel(value = "CardInfo对象", description = "")
public class CardInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cardNo;

    private String prcId;

    private LocalDate openingDate;

    private BigDecimal balance;

    private String cardType;

    private String pinNum;

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
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    public String getPinNum() {
        return pinNum;
    }

    public void setPinNum(String pinNum) {
        this.pinNum = pinNum;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
            "cardNo=" + cardNo +
            ", prcId=" + prcId +
            ", openingDate=" + openingDate +
            ", balance=" + balance +
            ", cardType=" + cardType +
            ", pinNum=" + pinNum +
        "}";
    }
}
