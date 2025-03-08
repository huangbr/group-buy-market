package edu.jnu.domain.trade.service;

import edu.jnu.domain.trade.adapter.repository.ITradeReposity;
import edu.jnu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import edu.jnu.domain.trade.model.entity.MarketPayOrderEntity;
import edu.jnu.domain.trade.model.entity.PayActivityEntity;
import edu.jnu.domain.trade.model.entity.PayDiscountEntity;
import edu.jnu.domain.trade.model.entity.UserEntity;
import edu.jnu.domain.trade.model.valobj.GroupBuyProgressVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TradeOrderServiceImpl implements ITradeOrderService{

    @Resource
    private ITradeReposity tradeReposity;

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
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) {
        log.info("拼团交易-锁定订单 userId:{} activityId:{} goodsId:{}", userEntity.getUserId(), payActivityEntity.getActivityId(), payDiscountEntity.getGoodsId());

        // 构造聚合实体对象
        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivityEntity)
                .payDiscountEntity(payDiscountEntity)
                .build();

        // 锁定订单：这会用户只是下单还没有支付。后续会有2个流程；支付成功、超时未支付（回退）
        MarketPayOrderEntity marketPayOrderEntity = tradeReposity.lockMarketPayOrder(groupBuyOrderAggregate);

        return marketPayOrderEntity;
    }
}
