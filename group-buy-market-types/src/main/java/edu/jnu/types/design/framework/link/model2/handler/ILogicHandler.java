package edu.jnu.types.design.framework.link.model2.handler;

/**
 * 逻辑处理器
 */
public interface ILogicHandler<T,D,R> {

    // 返回null表示将进入下一个链结点(此函数本身不进行节点跳转)
    default R next(T requestParameter, D dynamicContext){
        return null;
    }

    // 执行本节点的业务逻辑
    R apply(T requestParameter, D dynamicContext) throws Exception;

}
