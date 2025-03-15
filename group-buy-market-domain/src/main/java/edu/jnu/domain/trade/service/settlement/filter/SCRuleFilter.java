package edu.jnu.domain.trade.service.settlement.filter;

import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import edu.jnu.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import edu.jnu.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import edu.jnu.types.design.framework.link.model2.handler.ILogicHandler;
import edu.jnu.types.enums.ResponseCode;
import edu.jnu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SC渠道来源过滤 - 当某个签约渠道下架后，当作黑名单渠道过滤掉
 */
@Slf4j
@Service
public class SCRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeReposity tradeReposity;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("结算规则过滤-渠道黑名单校验{}， outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // sc渠道黑名单拦截
        boolean isIntercept = tradeReposity.isSCBlackIntercept(requestParameter.getSource(), requestParameter.getChannel());
        if(isIntercept){
            log.error("{}{} 渠道黑名单拦截",requestParameter.getSource(), requestParameter.getChannel());
            throw new AppException(ResponseCode.E0105);
        }

        return next(requestParameter, dynamicContext);
    }
}
