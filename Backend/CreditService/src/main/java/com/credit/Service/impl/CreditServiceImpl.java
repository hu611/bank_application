package com.credit.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.base.util.DecryptUtils;
import com.base.util.JsonUtils;
import com.credit.Utils.Constants;
import com.credit.Utils.UsefulFunctions;
import com.credit.mapper.*;
import com.base.pojo.*;
import com.credit.Service.CreditService;
import com.credit.pojo.AuditCredit;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class CreditServiceImpl implements CreditService {
    @Autowired
    CreditCardBillMapper creditCardBillMapper;

    @Autowired
    CreditCardMapper creditCardMapper;

    @Autowired
    AuditCreditMapper auditCreditMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void initializeRedis() throws Exception{
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        redisConnection.openPipeline();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        initializeCreditBalanceRedis(redisSerializer, redisConnection);

        redisConnection.closePipeline();
    }

    /**
     * insert creditCardNum Balance to Redis
     */
    public void initializeCreditBalanceRedis(RedisSerializer redisSerializer, RedisConnection redisConnection) throws Exception {
        List<CreditCard> creditCardList = creditCardMapper.getAllCreditCards();
        for(CreditCard creditCard: creditCardList) {
            String key = UsefulFunctions.get_Balance_Redis(creditCard.getCardNo());
            redisConnection.set(redisSerializer.serialize(key), redisSerializer.serialize(creditCard.getBalance().toString()+","+creditCard.getQuota().toString()));
            System.out.println("inserted " + key + " to Redis");
        }
    }


    @Override
    public CreditCard getCreditCardByPrcId(String prcId) throws Exception {
        return creditCardMapper.getCreditCardByPrcId(prcId);
    }

    @Autowired
    CreditCardBillPaybackRecordMapper creditCardBillPaybackRecordMapper;

    @Override
    public float getCreditScore() throws Exception {
        return 0;
    }

    /**
     * 获得最低还款额
     * @param CreditCard
     * @return BigDecimal: 0.信用额度内消费金额×10% 1.预借现金交易金额×100% 2.前期最低还款额未还部分
     * 3.所有费用和利息×100% 4.最新的lowest payback amount
     */
    @Override
    public BigDecimal[] getLowestPayBackAmount(CreditCard creditCard) {
        BigDecimal[] ret = new BigDecimal[5];
        BigDecimal res = new BigDecimal(0);

        //信用额度内消费金额×10%
        BigDecimal balance_rate = creditCard.getBalance().multiply(new BigDecimal(0.1));
        ret[0] = balance_rate;
        res = res.add(balance_rate);

        //预借现金交易金额×100%
        BigDecimal cashAdvance_rate = creditCard.getCashAdvance();
        ret[1] = cashAdvance_rate;
        res = res.add(cashAdvance_rate);

        //前期最低还款额未还部分
        BigDecimal UnpaidMinRepayment_rate = creditCard.getUnpaidMinRepayment().add(creditCard.getUnpaidMinRepayment());
        ret[2] = UnpaidMinRepayment_rate;
        res = res.add(UnpaidMinRepayment_rate);

        //所有费用和利息×100%
        BigDecimal interestAmount_rate = creditCard.getInterestAmount();
        res = res.add(interestAmount_rate);
        ret[3] = interestAmount_rate;
        ret[4] = res;
        return ret;
    }

    /**
     * This will only return this month's compound interest for all unpaid bills
     *
     * @param prcId
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal getCreditCardCompoundInterest(String prcId) throws Exception {
        List<CreditCardBill> creditCardUnpaidBills = creditCardBillMapper.getCreditCardBillByPrcIdAndType(prcId, 0);
        CreditCard creditCard = creditCardMapper.getCreditCardByPrcId(prcId);
        BigDecimal res = new BigDecimal(0);
        for (CreditCardBill creditCardBill : creditCardUnpaidBills) {
            LocalDate owe_date = creditCardBill.getOweDate();
            if (owe_date.isAfter(creditCard.getLastBillDate())) {
                //免息日期范围内
                break;
            }
            //获得信用卡还钱记录
            List<CreditCardBillPaybackRecord> creditCardBillPaybackRecords
                    = creditCardBillPaybackRecordMapper.getRecordByBillIdBetweenDate(creditCardBill.getBillId()
                    , creditCard.getLastBillDate(), creditCard.getLastBillDate().plusMonths(1));

            BigDecimal[] bigDecimals = get_interest_amount(creditCardBill.getOweAmount(),
                    creditCardBillPaybackRecords, creditCard.getLastBillDate(), creditCard.getLastBillDate().plusMonths(1));
            res = res.add(bigDecimals[0]);

            if (owe_date.isAfter(creditCard.getLastBillDate().minusMonths(1))) {
                //上个周期是免息范围内，并且没有在这个周期付清
                //额外算前面因为免息没有算的利息

                //上个月的账单还款记录
                LocalDate start_date = creditCard.getLastBillDate().minusMonths(1);
                LocalDate end_date = creditCard.getLastBillDate();
                creditCardBillPaybackRecords
                        = creditCardBillPaybackRecordMapper.getRecordByBillIdBetweenDate(creditCardBill.getBillId(),
                        start_date, end_date);
                BigDecimal[] previous_month = get_interest_amount(bigDecimals[1],
                        creditCardBillPaybackRecords,start_date,end_date);
                res = res.add(previous_month[0]);
            }

        }
        return res;
    }

    /**
     * 因为一个月内可能会多次还钱，所以需要请求他的记录并且分开算,用于审计
     * @param owe_amount: end_date的时候当前bill欠钱总数
     * @param creditCardBillPaybackRecords date followed by desc
     * @param start_date
     * @param end_date
     * @return
     */
    public BigDecimal[] get_interest_amount(BigDecimal owe_amount,
                                          List<CreditCardBillPaybackRecord> creditCardBillPaybackRecords,
                                          LocalDate start_date,
                                          LocalDate end_date) {
        BigDecimal res = new BigDecimal(0);
        for(CreditCardBillPaybackRecord creditCardBillPaybackRecord: creditCardBillPaybackRecords) {
            int days = (int)ChronoUnit.DAYS.between(creditCardBillPaybackRecord.getPaybackDate(), end_date);
            BigDecimal add_interest = UsefulFunctions.calculate_interest(days, owe_amount);
            res = res.add(add_interest);
            owe_amount = owe_amount.add(creditCardBillPaybackRecord.getPaybackAmount());
            end_date = creditCardBillPaybackRecord.getPaybackDate();
        }

        int days = (int)ChronoUnit.DAYS.between(end_date,start_date);
        res = res.add(Constants.INTEREST_RATE.multiply(new BigDecimal(days)).multiply(owe_amount));
        return new BigDecimal[]{res,owe_amount};
    }

    @Override
    public boolean hasCreditCard(String prcId) throws Exception {
        CreditCard creditCard = creditCardMapper.getCreditCardByPrcId(prcId);
        return creditCard != null;
    }

    @Override
    public boolean creditPay(String aesString) {
        JsonNode jsonNode;
        try {
            jsonNode = DecryptUtils.aes_decrypt(aesString);
        } catch (Exception e) {
            return false;
        }
        String amount = JsonUtils.json_to_string(jsonNode, "amount");
        BigDecimal amountBD = new BigDecimal(amount);
        String cardNum = JsonUtils.json_to_string(jsonNode, "cardNum");
        String key = UsefulFunctions.get_Balance_Redis(cardNum);
        String shared_key = UsefulFunctions.get_Shared_Key(key);
        if(!acquire_key(redisTemplate, shared_key)) {
            return false;
        }
        String res = redisTemplate.opsForValue().get(key).toString();
        String[] resList = res.split(",");
        System.out.println(res);
        BigDecimal balance = new BigDecimal(resList[0]);
        BigDecimal quota = new BigDecimal(resList[1]);
        balance = balance.add(amountBD);

        //当前小于限额
        if(balance.compareTo(quota) != 1) {
            redisTemplate.opsForValue().set(key, balance.toString()+","+quota);
        } else {
            return false;
        }

        Object[] parameter = new Object[]{amountBD, cardNum};
        //更新数据库的信用卡balance
        FunctionThread sqlThread = new FunctionThread(creditCardMapper, "updateBalance", parameter);
        sqlThread.start();


        //添加账单信息
        CreditCardBill creditCardBill = new CreditCardBill();
        creditCardBill.setPaid(0);
        creditCardBill.setBillName("Credit Card Payment");
        creditCardBill.setOweAmount(amountBD);
        creditCardBill.setBillTotal(amountBD);
        creditCardBill.setPrcId("11");
        creditCardBill.setOweDate(LocalDate.now());
        creditCardBillMapper.insert(creditCardBill);
        return true;
    }

    public static boolean acquire_key(RedisTemplate redisTemplate, String key) {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        boolean acquired = redisConnection.setNX(key.getBytes(StandardCharsets.UTF_8), new byte[0]);
        int i = 0;
        while(!acquired) {
            i++;
            acquired = redisConnection.setNX(key.getBytes(StandardCharsets.UTF_8), new byte[0]);
            if(i > 10) {
                //retry 10 times
                return false;
            }
        }

        redisConnection.pExpire(key.getBytes(),100);
        return true;
    }

    class FunctionThread extends Thread {
        Object mapper;
        String method;
        Object[] parameters; // Array of parameters

        public FunctionThread(Object mapper, String method, Object[] parameters) {
            this.method = method;
            this.mapper = mapper;
            this.parameters = parameters;
        }

        @Override
        public void run() {
            try {
                // Get the Class object of the mapper
                Class<?> mapperClass = mapper.getClass();

                // Create an array of Class objects representing the parameter types
                Class<?>[] parameterTypes = new Class<?>[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameterTypes[i] = parameters[i].getClass();
                }

                // Get the Method object of the specified method name with parameter types
                Method methodObj = mapperClass.getMethod(method, parameterTypes);

                // Invoke the method on the mapper object with the specified parameters
                methodObj.invoke(mapper, parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    @Override
    public void registerCreditCard(String aesString) {
        try {
            JsonNode jsonNode = DecryptUtils.aes_decrypt(aesString);
            int creditScore = JsonUtils.json_to_int(jsonNode, "creditScore");
            String prcId = JsonUtils.json_to_string(jsonNode, "prcId");
            //get pin number from audit database
            List<AuditCredit> auditCreditList = auditCreditMapper.selectByPrcId(prcId);
            String pinNum = auditCreditList.get(0).getPinNum();
            //remove record from audit database
            for(AuditCredit auditCredit: auditCreditList) {
                auditCreditMapper.deleteById(auditCredit.getAuditCreditId());
            }
            //generate credit card
            CreditCard creditCard = new CreditCard();
            creditCard.setCardNo(generateAccountNum(prcId));
            creditCard.setLastBillDate(LocalDate.now());
            creditCard.setPrcId(prcId);
            creditCard.setPinNum(pinNum);
            creditCard.setOpeningDate(LocalDate.now());
            //assign quota and interest rate based on credit score
            //300 -850
            if(creditScore < 500) {
                creditCard.setQuota(new BigDecimal("5000"));
            } else {
                creditCard.setQuota(new BigDecimal("30000"));
            }
            creditCardMapper.insert(creditCard);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateAccountNum(String prcId) {
        String res = Constants.Credit_Card_NUMBER_PREFIX + prcId.substring(prcId.length()-3);
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 9; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        res = res + stringBuilder.toString();
        res = res + calculate_final_bank_account_no_digit(res);
        return res;

    }

    public char calculate_final_bank_account_no_digit(String currbankAccount) {
        int first_step_calc = 0;
        int index = 0;
        int second_step_calc = 0;
        for(int i = currbankAccount.length()-1; i >= 0; i--) {
            int curr_digit = currbankAccount.charAt(i)-'0';
            if(index % 2 == 0) {
                int tmp = curr_digit * 2;
                first_step_calc += tmp/10 + tmp%10;
            } else {
                second_step_calc += curr_digit;
            }
            index++;
        }
        int res = 10 - ((first_step_calc+second_step_calc) % 10);
        return (char) res;
    }
}


