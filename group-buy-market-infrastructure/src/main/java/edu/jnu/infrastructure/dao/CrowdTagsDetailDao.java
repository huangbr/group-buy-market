package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.CrowdTagsDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人群标签明细
 */


@Mapper
public interface CrowdTagsDetailDao {
    // 新增某个用户的标签
    void addCrowdTagsUserId(CrowdTagsDetail crowdTagsDetailReq);
}
