package edu.jnu.domain.trade.service.settlement.filter;

import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.entity.MarketPayOrderEntity;
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
 * 外部交易单号有效性过滤；外部交易单号是否为退单
 */
@Slf4j
@Service
public class OutTradeNoRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeReposity tradeReposity;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("结算规则过滤-外部交易单号校验{} outTradeNo:{}",requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // 查询拼团信息
        MarketPayOrderEntity marketPayOrderEntity = tradeReposity.queryMarketPayOrderEntityByOutTradeNo(requestParameter.getUserId(), requestParameter.getOutTradeNo());
        if (null == marketPayOrderEntity) {
            log.error("不存在的外部交易单号或用户已退单，不需要做支付订单结算:{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());
            throw new AppException(ResponseCode.E0104);
        }

        dynamicContext.setMarketPayOrderEntity(marketPayOrderEntity);

        return next(requestParameter, dynamicContext);
    }
}
