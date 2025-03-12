package edu.jnu.domain.activity.service.discount;

import edu.jnu.domain.activity.adapter.repository.IActivityRepository;
import edu.jnu.domain.activity.model.valobj.DisconutTypeEnum;
import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 折扣计算服务抽象类
 */
@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{

    @Resource
    protected IActivityRepository activityRepository;

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount){
        // 人群标签过滤
        if(DisconutTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowRange = filterTagId(userId,groupBuyDiscount.getTagId());
            if(!isCrowRange){
                log.info("折扣优惠计算拦截，用户不再优惠人群标签范围内 userId:{}", userId);
                return originalPrice;
            }
        }

        // 计算折扣后的价格
        return doCalculate(originalPrice, groupBuyDiscount);
    }

    //人群过滤：限定人群优惠
    private boolean filterTagId(String userId, String tagId){
        return activityRepository.isTagCrowdRange(tagId, userId);
    }

    // 优惠折扣计算
    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

}
