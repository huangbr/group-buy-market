package edu.jnu.types.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 多线程异步的策略路由器
 */
public abstract class AbstractMultiThreadStrategyRouter<T,D,R> implements StrategyHandler<T,D,R>,StrategyMapper<T,D,R>  {
    @Getter
    @Setter
    protected StrategyHandler<T,D,R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter, D dynamicContext) throws Exception{
        // 获取下一个树节点
        StrategyHandler<T,D,R> strategyHandler = get(requestParameter, dynamicContext);
        if(strategyHandler!=null){
            // 执行下一个树节点的逻辑
            return strategyHandler.apply(requestParameter,dynamicContext);
        }
        return defaultStrategyHandler.apply(requestParameter,dynamicContext);
    }

    @Override
    public R apply(T requestParameter, D dynamicContext) throws Exception {

        // 异步加载数据
        multiThread(requestParameter, dynamicContext);

        // 业务流程受理
        return doApply(requestParameter, dynamicContext);

    }

    /** 异步加载数据 */
    protected abstract void multiThread(T requestParameter, D dynamicContext) throws ExecutionException, InterruptedException, TimeoutException;
    /** 业务流程受理 */
    protected abstract R doApply(T requestParameter, D dynamicContext) throws Exception;




}

