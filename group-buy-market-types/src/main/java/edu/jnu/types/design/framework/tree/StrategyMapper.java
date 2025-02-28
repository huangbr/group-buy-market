package edu.jnu.types.design.framework.tree;

/**
 * 策略映射器：目的是选择对应的策略处理器
 */
public interface StrategyMapper<T,D,R> {
    /**
     * 获取待执行策略
     *
     * @param requestParameter 入参
     * @param dynamicContext   上下文
     * @return 返参
     * @throws Exception 异常
     */
    StrategyHandler<T,D,R> get(T requestParameter, D dynamicContext) throws Exception;

}
