package com.storage.Controller;

import com.base.RestResponse;
import com.base.pojo.DebitPlan;
import com.storage.Constants;
import com.storage.service.DebitPlanService;
import com.storage.service.utils.UsefulUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/plan")
public class DebitPlanController implements InitializingBean {

    @Autowired
    DebitPlanService debitPlanService;

    @Autowired
    @Qualifier(value = "DebitPlanRedisTemplate")
    RedisTemplate redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        //把计划都储存进redis
        List<DebitPlan> planServiceList = debitPlanService.getAllEffectiveDebitPlans();
        //删除redis里面现有的计划数据
        redisTemplate.delete(Constants.redis_debit_plan_key);
        System.out.println("Adding debit card plan information to redis....");
        int i = 0;
        // 获取 ListOperations 对象
        for(DebitPlan debitPlan: planServiceList) {
            long day = ChronoUnit.DAYS.between(LocalDate.now(),debitPlan.getExpireDate());
            redisTemplate.opsForValue().set(UsefulUtils._get_redis_debit_plan_key(i),debitPlan,day, TimeUnit.DAYS);
            i++;
        }

        System.out.println("Add operation completed");
    }

    @GetMapping("/getAllPlans")
    @ResponseBody
    public RestResponse getAllPlans() {
        int i = 0;
        List<DebitPlan> debitPlanList = new ArrayList<>();
        while (redisTemplate.hasKey(UsefulUtils._get_redis_debit_plan_key(i))) {
            System.out.println(UsefulUtils._get_redis_debit_plan_key(i));
            Object object = redisTemplate.opsForValue().get(UsefulUtils._get_redis_debit_plan_key(i));
            try {
                DebitPlan debitPlan = (DebitPlan) object;
                //这里plan id设置成redis的，这样查询直接导向redis就行了，也可以避免用户访问已经过期的借记卡计划
                debitPlan.setPlanId(i);
                debitPlanList.add(debitPlan);
            } catch (Exception e) {
                throw new RuntimeException("Error while converting redis object to debit plan");
            }
            i++;
        }
        return RestResponse.success(debitPlanList);
    }
}
