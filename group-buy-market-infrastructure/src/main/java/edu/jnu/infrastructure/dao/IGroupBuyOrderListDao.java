package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    // 更新订单明细的状态为已完成
    int updateOrderStatus2COMPLETE(GroupBuyOrderList groupBuyOrderListReq);

    // 查询拼团队伍中的所有外部交易单号
    List<String> queryGroupBuyCompleteOrderOutTradeNoListByTeamId(String teamId);

}
