package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.Sku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISkuDao {
    Sku querySkuByGoodsId(String goodsId);


}
