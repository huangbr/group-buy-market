package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.CrowdTags;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人群标签
 */


@Mapper  // 注解用于标识MyBatis的Mapper接口
public interface CrowdTagsDao {
    // 更新人群标签统计量
    void updateCrowdTagsStatistics(CrowdTags crowdTagsReq);
}
