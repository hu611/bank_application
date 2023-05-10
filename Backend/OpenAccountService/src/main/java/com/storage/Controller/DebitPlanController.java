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
        redisTemplate.opsForList().range(Constants.redis_debit_plan_key,0,-1);
        return null;
    }
}
