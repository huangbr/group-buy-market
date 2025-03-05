package edu.jnu.domain.Tag.adapter.repository;

import edu.jnu.domain.Tag.model.entity.CrowdTagsJobEntity;

/**
 * 人群标签仓储接口：接口不暴露dao操作
 */

public interface ITagRepository {

    void updateCrowdTagsStatistics(String tagId, int count);

    void addCrowdTagsUserId(String tagId, String userId);

    CrowdTagsJobEntity queryCrowdTagsJob(String tagId, String batchId);

}
