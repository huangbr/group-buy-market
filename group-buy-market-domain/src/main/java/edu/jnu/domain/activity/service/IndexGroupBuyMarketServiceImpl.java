package edu.jnu.domain.activity.service;

import edu.jnu.domain.activity.model.entity.MarketProductEntity;
import edu.jnu.domain.activity.model.entity.TrialBalanceEntity;
import edu.jnu.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import edu.jnu.types.design.framework.tree.StrategyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 首页营销服务
 */
//@Service
//public class IIndexGroupBuyMarketServiceImpl implements IIndexGroupBuyMarketService {
//    @Resource
//    private DefaultActivityStrategyFactory defaultActivityStrategyFactory;
//
//    @Override
//    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {
//
//        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler = defaultActivityStrategyFactory.strategyHandler();
//
//        TrialBalanceEntity trialBalanceEntity = strategyHandler.apply(marketProductEntity, new DefaultActivityStrategyFactory.DynamicContext());
//
//        return trialBalanceEntity;
//    }
//
//
//}
@Service
public class IndexGroupBuyMarketServiceImpl implements IIndexGroupBuyMarketService {

    @Resource
    private DefaultActivityStrategyFactory defaultActivityStrategyFactory;

    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {
        // 选择策略
        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler = defaultActivityStrategyFactory.strategyHandler();
        // 执行策略
        TrialBalanceEntity trialBalanceEntity = strategyHandler.apply(marketProductEntity, new DefaultActivityStrategyFactory.DynamicContext());

        return trialBalanceEntity;
    }

}