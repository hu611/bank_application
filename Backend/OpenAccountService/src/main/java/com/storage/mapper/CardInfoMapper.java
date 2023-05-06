package com.storage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storage.pojo.CardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author weiyanhu
 * @since 2023-04-09
 */
@Mapper
public interface CardInfoMapper extends BaseMapper<CardInfo> {
    public int count_card_no_by_prcId(Map<String, Object> map);

    public List<String> getCardByPrcId(String prcId);

    public CardInfo selectCardByCardIdForUpdate(String cardId);
}
