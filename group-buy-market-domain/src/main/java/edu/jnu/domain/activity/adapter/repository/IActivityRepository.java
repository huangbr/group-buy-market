package edu.jnu.domain.activity.adapter.repository;

import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import edu.jnu.domain.activity.model.valobj.SCSkuActivityVO;
import edu.jnu.domain.activity.model.valobj.SkuVO;

/**
 * 活动仓储接口：接口中不暴露dao操作
 */
public interface IActivityRepository {
    // 查询活动折扣
    GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId);

    // 查询Sku
    SkuVO querySkuByGoodsId(String goodsId);

    // 查询活动
    SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId);


}
