package edu.jnu.domain.trade.service;

import edu.jnu.domain.trade.model.entity.MarketPayOrderEntity;
import edu.jnu.domain.trade.model.entity.PayActivityEntity;
import edu.jnu.domain.trade.model.entity.PayDiscountEntity;
import edu.jnu.domain.trade.model.entity.UserEntity;
import edu.jnu.domain.trade.model.valobj.GroupBuyProgressVO;

public interface ITradeOrderService {

    // 查询未被支付的订单（已锁单，但未支付）
    MarketPayOrderEntity queryUnPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo);

    // 查询拼团进度
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    // 锁单（预支付订单）
    MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) throws Exception;


}
