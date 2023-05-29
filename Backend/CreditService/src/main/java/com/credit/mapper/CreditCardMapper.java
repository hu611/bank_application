package com.credit.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.pojo.CreditCard;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-20
 */
@Mapper
public interface CreditCardMapper extends BaseMapper<CreditCard> {
    CreditCard getCreditCardByPrcId(String prcId);

    List<CreditCard> getAllCreditCards();

    int updateInterest(BigDecimal interest, String prcId);

    int updateBalance(BigDecimal addedAmount, String accountNum);

    List<CreditCard> selectAllUnpaidMinCreditCards();

    int updateLateFee(BigDecimal late_fee, String card_no);
}
