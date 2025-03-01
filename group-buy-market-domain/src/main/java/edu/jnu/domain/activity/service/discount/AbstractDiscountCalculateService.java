package edu.jnu.domain.activity.service.discount;

import edu.jnu.domain.activity.model.valobj.DisconutTypeEnum;
import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * 折扣计算服务抽象类
 */
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{
    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount){
        // 人群标签过滤
        if(DisconutTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowRange = filterTagId(userId,groupBuyDiscount.getTagId());
            if(!isCrowRange){
                return originalPrice;
            }
        }

        // 计算折扣后的价格
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    //人群过滤：限定人群优惠
    private boolean filterTagId(String userId, String tagId){
        return true;
    }

    // 优惠折扣计算
    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

}
