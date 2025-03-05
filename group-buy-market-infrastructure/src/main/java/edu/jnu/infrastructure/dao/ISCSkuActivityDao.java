package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.SCSkuActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 渠道商品活动Dao
 */
@Mapper
public interface ISCSkuActivityDao {
    // 活动查询
    SCSkuActivity querySCSkuActivityBySCGoodsId(SCSkuActivity scSkuActivity);
}
