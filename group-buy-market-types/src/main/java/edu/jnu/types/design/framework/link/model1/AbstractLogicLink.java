package edu.jnu.types.design.framework.link.model1;

public abstract class AbstractLogicLink<T,D,R> implements  ILogicLink<T,D,R>{

    // 下一个链结点
    private ILogicLink<T,D,R> nextNode;

    @Override
    public ILogicLink<T, D, R> next() {
        return nextNode;
    }

    @Override
    public ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> nextNode) {
        this.nextNode = nextNode;
        return nextNode;
    }

    protected R next(T requestParameter, D dynamicContext) throws Exception{
        return nextNode.apply(requestParameter, dynamicContext);
    }




}
