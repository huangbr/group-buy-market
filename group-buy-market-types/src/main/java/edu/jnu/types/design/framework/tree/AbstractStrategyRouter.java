package edu.jnu.types.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * 策略路由器：通过调用 StrategyMapper 的 get 方法来选择合适的 StrategyHandler，并执行其 apply 方法
 */
public abstract class AbstractStrategyRouter<T,D,R> implements StrategyHandler<T,D,R>,StrategyMapper<T,D,R> {
    @Getter
    @Setter
    protected StrategyHandler<T,D,R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter, D dynamicContext) throws Exception{
        StrategyHandler<T,D,R> strategyHandler = get(requestParameter, dynamicContext);
        if(strategyHandler!=null){
            strategyHandler.apply(requestParameter,dynamicContext);
        }
        return defaultStrategyHandler.apply(requestParameter,dynamicContext);
    }
}
