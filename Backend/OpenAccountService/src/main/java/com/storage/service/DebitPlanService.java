package com.storage.service;

import com.base.pojo.DebitPlan;

import java.util.List;

public interface DebitPlanService {
    //获取所有还在生效的计划
    public List<DebitPlan> getAllEffectiveDebitPlans();
}
