package edu.jnu.domain.trade.service.lock.facetory;

import edu.jnu.domain.trade.model.entity.GroupBuyActivityEntity;
import edu.jnu.domain.trade.model.entity.TradeLockRuleCommandEntity;
import edu.jnu.domain.trade.model.entity.TradeLockRuleFilterBackEntity;
import edu.jnu.domain.trade.service.lock.filter.ActivityUsabilityRuleFilter;
import edu.jnu.domain.trade.service.lock.filter.UserTakeLimitRuleFilter;
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
 * 交易锁单规则过滤工厂：定义锁单规则的责任链
 */

@Slf4j
@Service
public class TradeLockRuleFilterFactory {

    @Bean("tradeLockRuleFilter")
    public BusinessLinkedList<TradeLockRuleCommandEntity, DynamicContext, TradeLockRuleFilterBackEntity> tradeLockRuleFilter(ActivityUsabilityRuleFilter activityUsabilityRuleFilter,
                                                                                                                             UserTakeLimitRuleFilter userTakeLimitRuleFilter){
        // 组装链
        LinkArmory<TradeLockRuleCommandEntity, DynamicContext, TradeLockRuleFilterBackEntity> linkArmory =
                new LinkArmory<>("交易规则过滤责任链", activityUsabilityRuleFilter, userTakeLimitRuleFilter);

        // 链对象
        return linkArmory.getLogicLink();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        private GroupBuyActivityEntity groupBuyActivity;
    }
}
