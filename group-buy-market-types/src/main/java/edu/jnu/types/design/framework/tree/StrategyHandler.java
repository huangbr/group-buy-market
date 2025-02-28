package edu.jnu.types.design.framework.tree;

/**
 * 策略处理器：定义了具体业务逻辑的执行方式
 */
public interface StrategyHandler<T, D, R> {
    StrategyHandler DEFAULT = (T,D) -> null;
    R apply(T requestParameter, D dynamicContext) throws Exception;
}
