package com.credit.mapper;

import com.base.pojo.CreditCardBillPaybackRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-21
 */
@Mapper
public interface CreditCardBillPaybackRecordMapper extends BaseMapper<CreditCardBillPaybackRecord> {
    public List<CreditCardBillPaybackRecord> getRecordByBillIdBetweenDate(int bill_id, LocalDate early, LocalDate late);
}
