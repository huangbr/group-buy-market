package edu.jnu.domain.activity.service.discount.impl;

import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import edu.jnu.domain.activity.service.discount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 直减：在商品原价的基础上直降10元
 */
@Slf4j
@Service("ZJ")
public class ZJCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());

        // 获取折扣公式：得到直降的金额
        String marketExpr = groupBuyDiscount.getMarketExpr();

        // 现价 = 原价 - 直降金额
        BigDecimal deductionPrice = originalPrice.subtract(new BigDecimal(marketExpr));

        // 判断折扣后金额，最低支付1分钱
        if(deductionPrice.compareTo(BigDecimal.ZERO)<=0){
            return new BigDecimal("0.01");
        }

        return deductionPrice;
    }
}
