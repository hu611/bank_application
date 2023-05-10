package com.storage.mapper;

import com.base.pojo.DebitPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-05-10
 */
@Mapper
public interface DebitPlanMapper extends BaseMapper<DebitPlan> {
    public List<DebitPlan> getAllEffectiveDebitPlan();
}
