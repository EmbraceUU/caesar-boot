package com.demo.algorithms.list;

/**
 * 使其可以删除某个链表中给定的（非末尾）节点
 **/
public class DeleteNode {

    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
}
