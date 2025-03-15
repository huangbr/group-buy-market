package edu.jnu.domain.activity.service.trial.node;

import com.alibaba.fastjson.JSON;
import edu.jnu.domain.activity.model.entity.MarketProductEntity;
import edu.jnu.domain.activity.model.entity.TrialBalanceEntity;
import edu.jnu.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import edu.jnu.domain.activity.model.valobj.TagScopeEnumVO;
import edu.jnu.domain.activity.service.AbstractGroupBuyMarketSupport;
import edu.jnu.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import edu.jnu.types.common.Constants;
import edu.jnu.types.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TagNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private EndNode endNode;

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-TagNode userId:{} requestParameter:{}",requestParameter.getUserId(), JSON.toJSONString(requestParameter));
        GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = dynamicContext.getGroupBuyActivityDiscountVO();
        String tagId = groupBuyActivityDiscountVO.getTagId();
        String tagScope = groupBuyActivityDiscountVO.getTagScope();
        String userId = requestParameter.getUserId();


        // 默认配置的可见性、可参与性
        boolean defaultVisible = TagScopeEnumVO.fromCode(tagScope).isVisible();
        boolean defaultEnable = TagScopeEnumVO.fromCode(tagScope).isEnable();

        // 没有配置人群标签，则所有用户可见可参与
        if(StringUtils.isBlank(tagId)){
            dynamicContext.setVisible(true);
            dynamicContext.setEnable(true);
            return router(requestParameter, dynamicContext);
        }

        // 判断用户是否在人群标签范围内：查bitMap
        boolean isWithin = activityRepository.isTagCrowdRange(tagId, userId);

        /*
        如果拼团活动默认是可见的（visible == true），那么无论用户是否在人群范围内，活动都应该是可见的;
        如果拼团活动默认不可见（visible == false），则只有当用户在人群范围内时，活动才可见。
        如果拼团活动默认是可参与的（enable == true），那么无论用户是否在人群范围内，活动都应该是可参与的。
        如果拼团活动默认不可参与（enable == false），则只有当用户在人群范围内时，活动才可参与。
         */
        dynamicContext.setVisible(defaultVisible || isWithin);
        dynamicContext.setEnable(defaultEnable || isWithin);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {

        return endNode;
    }
}
