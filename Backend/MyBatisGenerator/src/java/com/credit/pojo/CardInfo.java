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
 * @since 2023-05-10
 */
@TableName("card_info")
@ApiModel(value = "CardInfo对象", description = "")
public class CardInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cardNo;

    private String prcId;

    @ApiModelProperty("冻结资金")
    private BigDecimal freezeBalance;

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
    public BigDecimal getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(BigDecimal freezeBalance) {
        this.freezeBalance = freezeBalance;
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
            ", freezeBalance=" + freezeBalance +
            ", openingDate=" + openingDate +
            ", balance=" + balance +
            ", cardType=" + cardType +
            ", pinNum=" + pinNum +
        "}";
    }
}
