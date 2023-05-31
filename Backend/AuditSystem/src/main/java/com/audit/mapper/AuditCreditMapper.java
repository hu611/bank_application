package com.audit.mapper;

import com.audit.dto.CreditAuditResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.audit.pojo.AuditCredit;
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
    public List<CreditAuditResponse> getAllAuditCredits();

}
