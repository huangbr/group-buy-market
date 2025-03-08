package edu.jnu.types.design.framework.link.model1;

/**
 * 责任链的装配：提供添加节点方法、获取节点的接口,负责节点之间的连接和遍历
 *
 * @param <T> requestParameter 请求参数
 * @param <D> dynamicContext 动态上下文
 * @param <R> response 响应值
 */


public interface ILogicChainArmory<T, D, R> {
    // 跳转到下一节点
    ILogicLink<T,D,R> next();

    // 添加节点
    ILogicLink<T,D,R> appendNext(ILogicLink<T,D,R> nextNode);
}
