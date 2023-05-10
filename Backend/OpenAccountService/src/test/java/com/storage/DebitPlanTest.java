package com.storage;

import com.storage.Controller.DebitPlanController;
import com.storage.mapper.DebitPlanMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DebitPlanTest {
    @Autowired
    DebitPlanMapper debitPlanMapper;

    @Autowired
    DebitPlanController debitPlanController;

    @Test
    public void testAllEffectivePlan() {
        System.out.println(debitPlanMapper.getAllEffectiveDebitPlan());
    }

    @Test
    public void testDebitPlanController() {
        debitPlanController.getAllPlans();
    }
}
