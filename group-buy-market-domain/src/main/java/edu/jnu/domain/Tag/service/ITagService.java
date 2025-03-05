package edu.jnu.domain.Tag.service;

/**
 * 人群标签服务接口
 */
public interface ITagService {

    // 执行人群标签批次任务:满足任务条件的用户将被打上指定标签
    void execTagBatchJob(String tagId, String batchId);
}
