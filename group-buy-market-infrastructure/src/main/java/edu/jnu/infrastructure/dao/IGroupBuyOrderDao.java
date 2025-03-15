package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户拼单Dao
 */

@Mapper
public interface IGroupBuyOrderDao {
    // 插入拼团订单
    void insert(GroupBuyOrder groupBuyOrder);

    // 增加订单锁单量
    int updateAddLockCount(String teamId);

    // 减少订单锁单量
    int updateSubtractionLockCount(String teamId);

    // 查询拼团进度
    GroupBuyOrder queryGroupBuyProgress(String teamId);

    // 查询拼团队伍
    GroupBuyOrder queryGroupBuyTeamByTeamId(String Id);

    // 增加拼团支付完成人数
    int updateAddCompleteCount(String teamId);

    // 更新订单状态为已完成
    int updateOrderStatus2COMPLETE(String teamId);
}
