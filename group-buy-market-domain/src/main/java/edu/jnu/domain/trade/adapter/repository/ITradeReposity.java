package edu.jnu.domain.trade.adapter.repository;

import edu.jnu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import edu.jnu.domain.trade.model.entity.MarketPayOrderEntity;
import edu.jnu.domain.trade.model.valobj.GroupBuyProgressVO;

/**
 * 交易仓储服务接口
 */

public interface ITradeReposity {

    // 查询拼团订单支付明细
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);

    // 锁单
    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    // 查询拼团进度
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

}
