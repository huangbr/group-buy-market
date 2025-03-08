package edu.jnu.types.design.framework.link.model1;

/**
 * 责任链节点的处理逻辑：扩展责任链接口，增加业务逻辑处理能力，定义每个节点的具体行为。
 *
 * @param <T> requestParameter 请求参数
 * @param <D> dynamicContext 动态上下文
 * @param <R> response 响应值
 */

public interface ILogicLink<T, D, R> extends ILogicChainArmory<T,D,R>{
    // 逻辑处理执行器: 执行本节点的业务逻辑
    R apply(T requestParameter, D dynamicContext) throws Exception;
}
