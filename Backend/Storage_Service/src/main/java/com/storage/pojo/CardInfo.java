package com.storage.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

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
@Data
@ToString
public class CardInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String cardNo;

    private String prcId;

    private LocalDate openingDate;

    private BigDecimal balance;

    private String cardType;

    private String pinNum;

}
