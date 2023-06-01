package com.credit.mapper;

import com.credit.pojo.AuditCredit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-25
 */
@Mapper
public interface AuditCreditMapper extends BaseMapper<AuditCredit> {
    public List<AuditCredit> selectByPrcId(String prcId);

}
