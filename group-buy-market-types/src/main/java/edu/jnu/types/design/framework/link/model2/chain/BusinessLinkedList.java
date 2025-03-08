package edu.jnu.types.design.framework.link.model2.chain;

import edu.jnu.types.design.framework.link.model2.handler.ILogicHandler;

/**
 *  业务链路
 */
public class BusinessLinkedList<T,D,R> extends LinkedList<ILogicHandler<T,D,R>> implements ILogicHandler<T,D,R> {

    public BusinessLinkedList(String name) {
        super(name);
    }

    @Override
    public R apply(T requestParameter, D dynamicContext) throws Exception {

        Node<ILogicHandler<T,D,R>> current = this.first;
        // 遍历的过程会以 apply 是否为空和链路是否走到最后来判断。apply 为空则表示当前ILogicHandler实现的节点的业务流程为空，放行到下一个节点。
        do{
            ILogicHandler<T,D,R> itemHandler = current.item;
            R apply = itemHandler.apply(requestParameter, dynamicContext);
            if(apply != null){
                return apply;
            }
            current = current.next;
        } while(current != null);

        return null;
    }
}
