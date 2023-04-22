package com.credit.mapper;

import com.base.pojo.DailyInterestAmountRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-22
 */
@Mapper
public interface DailyInterestAmountRecordMapper extends BaseMapper<DailyInterestAmountRecord> {

    public void add_record(DailyInterestAmountRecord dailyInterestAmountRecord);
}
