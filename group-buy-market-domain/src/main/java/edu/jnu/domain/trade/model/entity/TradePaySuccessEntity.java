package edu.jnu.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易-订单支付成功实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePaySuccessEntity {
    /** 来源 */
    private String source;
    /** 渠道 */
    private String channel;
    /** 用户Id */
    private String userId;
    /** 外部交易单号 */
    private String outTradeNo;
}
