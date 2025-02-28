/**
 * DAO 接口；IXxxDao
 */
package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.GroupBuyActivity;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 拼团活动DAO
 */
@Mapper
public interface IGroupBuyActivityDao{
    List<GroupBuyActivity> queryGroupBuyActivityList();

    GroupBuyActivity queryValidGroupBuyActivity(GroupBuyActivity groupBuyActivityReq);

}

