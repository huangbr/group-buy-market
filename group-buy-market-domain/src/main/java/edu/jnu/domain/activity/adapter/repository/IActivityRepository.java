package edu.jnu.domain.activity.adapter.repository;

import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import edu.jnu.domain.activity.model.valobj.SkuVO;

/**
 * 活动仓储
 */
public interface IActivityRepository {
    // 查询活动折扣
    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(String source, String channel);

    // 查询Sku
    SkuVO querySkuByGoodsId(String goodsId);

}
