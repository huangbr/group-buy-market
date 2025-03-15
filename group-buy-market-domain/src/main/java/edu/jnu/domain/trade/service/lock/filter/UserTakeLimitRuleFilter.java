package edu.jnu.domain.trade.service.lock.filter;

import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.entity.GroupBuyActivityEntity;
import edu.jnu.domain.trade.model.entity.TradeRuleCommandEntity;
import edu.jnu.domain.trade.model.entity.TradeRuleFilterBackEntity;
import edu.jnu.domain.trade.service.lock.facetory.TradeRuleFilterFactory;
import edu.jnu.types.design.framework.link.model2.handler.ILogicHandler;
import edu.jnu.types.enums.ResponseCode;
import edu.jnu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户参与限制的规则过滤器：限制用户对一个拼团活动的参与次数
 */

@Slf4j
@Service
public class UserTakeLimitRuleFilter implements ILogicHandler<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> {
    @Resource
    private ITradeReposity tradeReposity;


    @Override
    public TradeRuleFilterBackEntity apply(TradeRuleCommandEntity requestParameter, TradeRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("交易规则过滤-用户参与次数校验{} activityId:{}",requestParameter.getUserId(), requestParameter.getActivityId());;

        // 获取活动
        GroupBuyActivityEntity groupBuyActivity = dynamicContext.getGroupBuyActivity();

        // 查询用户在一个拼团活动上参与的次数
        int count = tradeReposity.queryOrderCountByActivityId(requestParameter.getActivityId(), requestParameter.getUserId());

        if(groupBuyActivity.getTakeLimitCount()!=null && count>=groupBuyActivity.getTakeLimitCount()){
            log.info("用户参与次数校验-已达到此活动的可参与上限 activityId:{}",requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0103);
        }

        return TradeRuleFilterBackEntity.builder()
                .userTakeOrderCount(count)
                .build();
    }
}
