package edu.jnu.domain.trade.model.aggregate;

import edu.jnu.domain.trade.model.entity.PayActivityEntity;
import edu.jnu.domain.trade.model.entity.PayDiscountEntity;
import edu.jnu.domain.trade.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 拼团订单聚合对象：聚合可以理解用各个四肢、身体、头等组装出来一个人
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyOrderAggregate {

    /** 用户实体对象 */
    private UserEntity userEntity;
    /** 支付活动实体对象 */
    private PayActivityEntity payActivityEntity;
    /** 支付优惠实体对象 */
    private PayDiscountEntity payDiscountEntity;
    /** 用户已参与的拼团次数 */
    private Integer userTakeOrderCount;
}
