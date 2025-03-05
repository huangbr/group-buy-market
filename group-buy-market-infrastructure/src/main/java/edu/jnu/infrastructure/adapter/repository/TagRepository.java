package edu.jnu.infrastructure.adapter.repository;

import edu.jnu.domain.Tag.adapter.repository.ITagRepository;
import edu.jnu.domain.Tag.model.entity.CrowdTagsJobEntity;
import edu.jnu.infrastructure.dao.CrowdTagsDao;
import edu.jnu.infrastructure.dao.CrowdTagsDetailDao;
import edu.jnu.infrastructure.dao.CrowdTagsJobDao;
import edu.jnu.infrastructure.dao.po.CrowdTags;
import edu.jnu.infrastructure.dao.po.CrowdTagsDetail;
import edu.jnu.infrastructure.dao.po.CrowdTagsJob;
import edu.jnu.infrastructure.redis.IRedisService;
import org.redisson.api.RBitSet;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.BitSet;

/**
 * 人群标签仓储：进行dao操作
 */

@Repository
public class TagRepository implements ITagRepository {
    @Resource
    private CrowdTagsDao crowdTagsDao;
    @Resource
    private CrowdTagsDetailDao crowdTagsDetailDao;
    @Resource
    private CrowdTagsJobDao crowdTagsJobDao;

    @Resource
    private IRedisService redisService;



    @Override
    public void updateCrowdTagsStatistics(String tagId, int count) {
        CrowdTags crowdTagsReq = new CrowdTags();
        crowdTagsReq.setTagId(tagId);
        crowdTagsReq.setStatistics(count);

        crowdTagsDao.updateCrowdTagsStatistics(crowdTagsReq);
    }

    @Override
    public void addCrowdTagsUserId(String tagId, String userId) {
        CrowdTagsDetail crowdTagsDetailReq = new CrowdTagsDetail();
        crowdTagsDetailReq.setTagId(tagId);
        crowdTagsDetailReq.setUserId(userId);
        try{
            // 1.向数据库中插入
            crowdTagsDetailDao.addCrowdTagsUserId(crowdTagsDetailReq);
            // 2.向Redis BitSet中插入，适合高并发场景判断用户是否存在
            // 每个tagId对应一个BitSet，表示与该tag相关的用户集合
            RBitSet bitSet = redisService.getBitSet(tagId);
            // 在位图中，每个用户都有一个唯一的索引位置，对应的位设置为 true，表示该用户属于当前标签
            bitSet.set(redisService.getIndexFromUserId(userId),true);

        }catch (DuplicateKeyException ignore) {
            // 忽略唯一索引冲突
        }
    }

    @Override
    public CrowdTagsJobEntity queryCrowdTagsJob(String tagId, String batchId) {
        CrowdTagsJob crowdTagsJobReq = new CrowdTagsJob();
        crowdTagsJobReq.setTagId(tagId);
        crowdTagsJobReq.setBatchId(batchId);

        CrowdTagsJob crowdTagsJobRes = crowdTagsJobDao.queryCrowdTagsJob(crowdTagsJobReq);

        if(crowdTagsJobRes==null){
            return null;
        }

        return CrowdTagsJobEntity.builder()
                .tagType(crowdTagsJobRes.getTagType())
                .tagRule(crowdTagsJobRes.getTagRule())
                .statStartTime(crowdTagsJobRes.getStatStartTime())
                .statEndTime(crowdTagsJobRes.getStatEndTime())
                .build();
    }
}
