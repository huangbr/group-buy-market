package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.Sku;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品Dao
 */
@Mapper
public interface ISkuDao {
    // 商品查询
    Sku querySkuByGoodsId(String goodsId);


}
