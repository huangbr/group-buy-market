package edu.jnu.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易-订单结算实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePaySettlementEntity {
    /** 来源 */
    private String source;
    /** 渠道 */
    private String channel;
    /** 用户Id */
    private String userId;
    /** 组队Id */
    private String teamId;
    /** 活动Id */
    private Long activityId;
    /** 外部交易单号 */
    private String outTradeNo;

}
