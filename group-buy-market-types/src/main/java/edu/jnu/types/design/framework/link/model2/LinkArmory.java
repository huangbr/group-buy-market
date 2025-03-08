package edu.jnu.types.design.framework.link.model2;

import edu.jnu.types.design.framework.link.model2.chain.BusinessLinkedList;
import edu.jnu.types.design.framework.link.model2.handler.ILogicHandler;

public class LinkArmory<T,D,R> {

    private final BusinessLinkedList<T,D,R> logicLink;

    public LinkArmory(String linkName, ILogicHandler<T,D,R>... logicHandlers){
        logicLink = new BusinessLinkedList<>(linkName);
        for(ILogicHandler<T,D,R> logicHandler: logicHandlers){
            logicLink.add(logicHandler);
        }
    }

    public BusinessLinkedList<T, D, R> getLogicLink() {
        return logicLink;
    }
}
