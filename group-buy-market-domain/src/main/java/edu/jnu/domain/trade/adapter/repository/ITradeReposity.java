package edu.jnu.domain.trade.adapter.repository;

import edu.jnu.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import edu.jnu.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import edu.jnu.domain.trade.model.entity.GroupBuyActivityEntity;
import edu.jnu.domain.trade.model.entity.GroupBuyTeamEntity;
import edu.jnu.domain.trade.model.entity.MarketPayOrderEntity;
import edu.jnu.domain.trade.model.valobj.GroupBuyProgressVO;

/**
 * 交易仓储服务接口
 */

public interface ITradeReposity {

    // 查询拼团订单：根据外部交易单号查询是否为拼团订单
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);

    // 锁单
    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    // 查询拼团进度
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    // 查询拼团活动实体
    GroupBuyActivityEntity queryGroupBuyActivityEntityByActivityId(Long activityId);

    // 查询用户在一个拼团活动上参与的次数
    Integer queryOrderCountByActivityId(Long activityId, String userId);

    // 查询拼团活动的组队信息
    GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId);

    // 拼团交易结算
    void settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate);

    // 是否为需要拦截的黑名单渠道
    boolean isSCBlackIntercept(String source, String channel);


}
