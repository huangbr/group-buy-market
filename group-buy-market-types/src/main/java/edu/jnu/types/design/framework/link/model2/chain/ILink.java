package edu.jnu.types.design.framework.link.model2.chain;

/**
 * 链表接口
 */

public interface ILink<E> {
    // 默认头插法
    boolean add(E e);

    // 头插法
    boolean addFirst(E e);

    // 尾插法
    boolean addLast(E e);

    // 删除节点
    boolean remove(Object o);

    // 获取节点
    E get(int index);

    // 打印链表
    void printLinkList();

}
