package com.demo.algorithms.list;

/**
 * reverse link list
 */
public class ReverseList {

    /**
     * Definition for singly-linked list.
     * @param head
     * @return
     */
    public static ListNode reverseList(ListNode head) {

        // 判断是否是最后两个节点，如果是左后两个节点，将指针所指的节点原样返回
        if (head == null || head.next == null) {
            return head;
        } else {
            // 如果不是最后两个节点，递归下一节点反转
            ListNode ln = reverseList(head.next);
            // 反转方法
            head.next.next = head;
            head.next = null;
            // 返回反转后的节点
            return ln;
        }
    }
}

