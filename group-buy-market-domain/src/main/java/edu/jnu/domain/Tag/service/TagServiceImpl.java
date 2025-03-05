package edu.jnu.domain.Tag.service;

import edu.jnu.domain.Tag.adapter.repository.ITagRepository;
import edu.jnu.domain.Tag.model.entity.CrowdTagsJobEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 人群标签服务
 */
@Slf4j
@Service
public class TagServiceImpl implements ITagService{
    @Resource
    private ITagRepository tagRepository;

    @Override
    public void execTagBatchJob(String tagId, String batchId) {

        // 1.查询人群标签任务
        CrowdTagsJobEntity crowdTagsJobEntity = tagRepository.queryCrowdTagsJob(tagId, batchId);

        // 2.采集用户数据：采集用户的消费类数据，后续有用户发起拼单后再处理。

        // 3.将采集到的用户Id写入记录
        List<String> userIdList = new ArrayList<String>(){{
                add("huangbr");
                add("xiaofuge");
                add("liergou");
            }
        };

        // 4.将人群标签明细写入数据库：一般人群标签的处理在公司中，会有专门的数据数仓团队通过脚本方式写入到数据库，就不用这样一个个或者批次来写。
        for(String userId:userIdList){
            tagRepository.addCrowdTagsUserId(tagId,userId);
        }

        // 5.更新人群标签统计量
        tagRepository.updateCrowdTagsStatistics(tagId,userIdList.size());
    }
}
