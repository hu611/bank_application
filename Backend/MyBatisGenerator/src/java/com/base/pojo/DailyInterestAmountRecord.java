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
 * @since 2023-04-22
 */
@TableName("daily_interest_amount_record")
@ApiModel(value = "DailyInterestAmountRecord对象", description = "")
public class DailyInterestAmountRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer recordId;

    private BigDecimal interestAmount;

    private LocalDate recordDate;

    private String prcId;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }
    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
    public String getPrcId() {
        return prcId;
    }

    public void setPrcId(String prcId) {
        this.prcId = prcId;
    }

    @Override
    public String toString() {
        return "DailyInterestAmountRecord{" +
            "recordId=" + recordId +
            ", interestAmount=" + interestAmount +
            ", recordDate=" + recordDate +
            ", prcId=" + prcId +
        "}";
    }
}
