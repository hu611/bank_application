package com.storage.service.impl;

import com.base.pojo.DebitPlan;
import com.storage.mapper.DebitPlanMapper;
import com.storage.service.DebitPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebitDebitPlanServiceImpl implements DebitPlanService {
    @Autowired
    DebitPlanMapper debitPlanMapper;

    @Override
    public List<DebitPlan> getAllEffectiveDebitPlans() {
        return debitPlanMapper.getAllEffectiveDebitPlan();
    }
}
