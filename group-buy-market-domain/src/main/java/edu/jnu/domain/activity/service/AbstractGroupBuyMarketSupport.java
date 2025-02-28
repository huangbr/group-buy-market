package edu.jnu.domain.activity.service;

import edu.jnu.domain.activity.adapter.repository.IActivityRepository;
import edu.jnu.types.design.framework.tree.AbstractMultiThreadStrategyRouter;
import edu.jnu.types.design.framework.tree.AbstractStrategyRouter;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 抽象的拼团营销支撑类
 */

public abstract class AbstractGroupBuyMarketSupport<MarketProductEntity, DynamicContext, TrialBalanceEntity> extends AbstractMultiThreadStrategyRouter<MarketProductEntity, DynamicContext, TrialBalanceEntity> {
    protected long timeout = 500;
    @Resource
    protected IActivityRepository activityRepository;

    // 缺省方法
    @Override
    protected void multiThread(MarketProductEntity requestParameter, DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException{


    }




}
