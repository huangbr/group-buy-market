package edu.jnu.domain.activity.service.discount.impl;

import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import edu.jnu.domain.activity.service.discount.AbstractDiscountCalculateService;
import edu.jnu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * 满减折扣：满100减10
 */
@Slf4j
@Service("MJ")
public class MJCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣类型：{}",groupBuyDiscount.getDiscountType().getCode());

        // 获取折扣公式：100,10 表示 满100减10
        String marketExpr = groupBuyDiscount.getMarketExpr();
        String[] split = marketExpr.split(Constants.SPLIT);  // 根据逗号进行分割
        BigDecimal x = new BigDecimal(split[0].trim());
        BigDecimal y = new BigDecimal(split[1].trim());

        // 若不满足折扣门槛价，则按原价计算
        if(originalPrice.compareTo(x)<0){
            return originalPrice;
        }

        // 计算折扣价：原价减y元
        BigDecimal deductionPrice = originalPrice.subtract(y);

        // 判断折扣后金额，最低支付1分钱
        if(deductionPrice.compareTo(BigDecimal.ZERO)<=0){
            return new BigDecimal("0.01");
        }

        return deductionPrice;

    }
}
