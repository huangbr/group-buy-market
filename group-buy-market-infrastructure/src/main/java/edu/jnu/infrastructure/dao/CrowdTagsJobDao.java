package edu.jnu.infrastructure.dao;

import edu.jnu.infrastructure.dao.po.CrowdTagsJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人群标签任务
 */


@Mapper
public interface CrowdTagsJobDao {
    // 查询人群标签任务
    CrowdTagsJob queryCrowdTagsJob(CrowdTagsJob crowdTagsJobReq);
}
