package com.storage.service.feign;

import com.base.pojo.CreditCard;
import feign.hystrix.FallbackFactory;

public class CreditCardFallBackFactory implements FallbackFactory<CreditCardFeign> {
    @Override
    public CreditCardFeign create(Throwable throwable) {
        return new CreditCardFeign() {
            @Override
            public boolean hasCreditCard(String prc_id) throws Exception {
                return false;
            }

            @Override
            public CreditCard getCreditCard(String prc_id) throws Exception {
                return null;
            }
        };
    }
}
