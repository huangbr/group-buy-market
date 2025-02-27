package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.GroupBuyDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGroupBuyDiscountDao {
    List<GroupBuyDiscount> queryGroupBuyDiscountList();

}
