package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户拼单明细Dao
 */

@Mapper
public interface IGroupBuyOrderListDao {

    // 插入拼团订单明细
    void insert(GroupBuyOrderList groupBuyOrderListReq);

    // 根据外部交易单号查询订单明细
    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(GroupBuyOrderList groupBuyOrderListReq);

    // 查询用户在一个拼团活动上已参与次数
    Integer queryOrderCountByActivityId(GroupBuyOrderList groupBuyOrderListReq);

}
