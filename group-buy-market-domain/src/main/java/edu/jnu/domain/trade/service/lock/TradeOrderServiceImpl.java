package edu.jnu.domain.trade.service.lock;

import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import edu.jnu.domain.trade.model.entity.*;
import edu.jnu.domain.trade.model.valobj.GroupBuyProgressVO;
import edu.jnu.domain.trade.service.ITradeLockOrderService;
import edu.jnu.domain.trade.service.lock.facetory.TradeRuleFilterFactory;
import edu.jnu.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TradeOrderServiceImpl implements ITradeLockOrderService {

    @Resource
    private ITradeReposity tradeReposity;
    @Resource
    private BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity> tradeRuleFilter;

    @Override
    public MarketPayOrderEntity queryUnPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        log.info("拼团交易-查询未支付的订单 userId:{} outTradeNo:{}",userId,outTradeNo);
        MarketPayOrderEntity marketPayOrderEntity = tradeReposity.queryMarketPayOrderEntityByOutTradeNo(userId,outTradeNo);
        return marketPayOrderEntity;
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        log.info("拼团交易-查询拼团进度 teamId:{}",teamId);
        GroupBuyProgressVO groupBuyProgressVO = tradeReposity.queryGroupBuyProgress(teamId);
        return groupBuyProgressVO;
    }

    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) throws Exception {
        log.info("拼团交易-锁定营销优惠订单 userId:{} activityId:{} goodsId:{}", userEntity.getUserId(), payActivityEntity.getActivityId(), payDiscountEntity.getGoodsId());

        // 1.责任链-交易规则过滤
        TradeRuleFilterBackEntity tradeRuleFilterBackEntity = tradeRuleFilter.apply(
                TradeRuleCommandEntity.builder()
                        .userId(userEntity.getUserId())
                        .activityId(payActivityEntity.getActivityId())
                        .build(),
                TradeRuleFilterFactory.DynamicContext.builder()
                        .build()
        );

        // 2.已参与的拼团次数：用于构建数据库唯一索引使用，确保用户只能在一个活动上参与固定的次数
        Integer userTakeOrderCount = tradeRuleFilterBackEntity.getUserTakeOrderCount();


        // 3.构造聚合实体对象
        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivityEntity)
                .payDiscountEntity(payDiscountEntity)
                .userTakeOrderCount(userTakeOrderCount)
                .build();

        // 4.锁定订单：这会用户只是下单还没有支付。后续会有2个流程；支付成功、超时未支付（回退）
        MarketPayOrderEntity marketPayOrderEntity = tradeReposity.lockMarketPayOrder(groupBuyOrderAggregate);

        return marketPayOrderEntity;
    }
}
