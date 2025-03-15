package edu.jnu.domain.trade.service.lock.filter;

import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.entity.GroupBuyActivityEntity;
import edu.jnu.domain.trade.model.entity.TradeLockRuleCommandEntity;
import edu.jnu.domain.trade.model.entity.TradeLockRuleFilterBackEntity;
import edu.jnu.domain.trade.service.lock.facetory.TradeLockRuleFilterFactory;
import edu.jnu.types.design.framework.link.model2.handler.ILogicHandler;
import edu.jnu.types.enums.ActivityStatusEnumVO;
import edu.jnu.types.enums.ResponseCode;
import edu.jnu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 活动可用性规则过滤器：根据活动状态、活动有效期进行过滤
 */
@Slf4j
@Service
public class ActivityUsabilityRuleFilter implements ILogicHandler<TradeLockRuleCommandEntity, TradeLockRuleFilterFactory.DynamicContext, TradeLockRuleFilterBackEntity> {

    @Resource
    private ITradeReposity tradeReposity;


    @Override
    public TradeLockRuleFilterBackEntity apply(TradeLockRuleCommandEntity requestParameter, TradeLockRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("交易规则过滤-活动可用性校验{} activityId:{}",requestParameter.getUserId(), requestParameter.getActivityId());

        // 查询拼团活动
        GroupBuyActivityEntity groupBuyActivity = tradeReposity.queryGroupBuyActivityEntityByActivityId(requestParameter.getActivityId());
        if(groupBuyActivity == null) return null;

        // 校验活动状态：合法状态为"活动生效"
        if(!ActivityStatusEnumVO.EFFECTIVE.equals(groupBuyActivity.getStatus())){
            log.info("活动可用性校验-非生效状态 activityId:{}",requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0101);
        }

        // 校验活动时间
        Date currentTime = new Date();
        if(currentTime.before(groupBuyActivity.getStartTime()) || currentTime.after(groupBuyActivity.getEndTime())){
            log.info("活动时间校验-非活动可参与时间范围 activityId:{}",groupBuyActivity);
            throw new AppException(ResponseCode.E0102);
        }

        // 写入动态上下文
        dynamicContext.setGroupBuyActivity(groupBuyActivity);

        // 走到下一个责任链节点
        return next(requestParameter, dynamicContext);

    }



}
