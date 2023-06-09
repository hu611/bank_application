package com.api.mapper;

import com.base.pojo.ApiRequestRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-05-16
 */
@Mapper
public interface ApiRequestRecordMapper extends BaseMapper<ApiRequestRecord> {
    public void updateResultById(int recordId);
}
