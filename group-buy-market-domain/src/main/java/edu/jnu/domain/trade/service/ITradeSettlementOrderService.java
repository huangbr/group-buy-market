package edu.jnu.domain.trade.service;

import edu.jnu.domain.trade.model.entity.TradePaySettlementEntity;
import edu.jnu.domain.trade.model.entity.TradePaySuccessEntity;

/**
 * 拼团交易结算服务接口
 */

public interface ITradeSettlementOrderService {

    // 营销订单的交易结算
    TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity);

}
