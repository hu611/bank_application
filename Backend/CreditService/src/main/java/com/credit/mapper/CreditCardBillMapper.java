package com.credit.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.pojo.CreditCardBill;
import org.apache.ibatis.annotations.Mapper;

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
public interface CreditCardBillMapper extends BaseMapper<CreditCardBill> {
    public List<CreditCardBill> getCreditCardBillByPrcIdAndType(String prcId, int type);
}
