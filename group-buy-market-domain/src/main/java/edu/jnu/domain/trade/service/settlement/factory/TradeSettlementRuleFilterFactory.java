package edu.jnu.domain.trade.service.settlement.factory;

import edu.jnu.domain.trade.model.entity.*;
import edu.jnu.domain.trade.service.settlement.filter.EndRuleFilter;
import edu.jnu.domain.trade.service.settlement.filter.OutTradeNoRuleFilter;
import edu.jnu.domain.trade.service.settlement.filter.SCRuleFilter;
import edu.jnu.domain.trade.service.settlement.filter.SettableRuleFilter;
import edu.jnu.types.design.framework.link.model2.LinkArmory;
import edu.jnu.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * 交易结算规则过滤工厂：定义结算规则的责任链
 */

@Slf4j
@Service
public class TradeSettlementRuleFilterFactory {

    @Bean("tradeSettlementRuleFilter")
    public BusinessLinkedList<TradeSettlementRuleCommandEntity, DynamicContext, TradeSettlementRuleFilterBackEntity> tradeSettlementRuleFilter(SCRuleFilter scRuleFilter,
                                                                                                                                               OutTradeNoRuleFilter outTradeNoRuleFilter,
                                                                                                                                               SettableRuleFilter settableRuleFilter,
                                                                                                                                               EndRuleFilter endRuleFilter){
        // 组装链
        LinkArmory<TradeSettlementRuleCommandEntity, DynamicContext, TradeSettlementRuleFilterBackEntity> linkArmory =
                new LinkArmory<>("结算规则过滤责任链", scRuleFilter, outTradeNoRuleFilter, settableRuleFilter, endRuleFilter);

        // 链对象
        return linkArmory.getLogicLink();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        // 订单营销实体对象
        private MarketPayOrderEntity marketPayOrderEntity;
        // 拼团组队实体对象
        private GroupBuyTeamEntity groupBuyTeamEntity;
    }
}
