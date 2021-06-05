package com.demo.algorithms.list;

/**
 * Judge palindromic linked lists
 */
public class IsPalindrome {
    /**
     * 判断回文链表
     *
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {
        // 如果链表为空或者仅有一个元素那么肯定是回文链表
        if (head == null || head.next == null) {
            return true;
        }
        // 快慢指针法，寻找链表中心，空间复杂度为O(1)
        ListNode slow, fast;
        slow = fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 使用反转链表，时间复杂度为O(n)
        if (fast != null) {
            // 链表元素奇数个
            slow.next = ReverseList.reverseList(slow.next);
            slow = slow.next;
        } else {
            // 链表元素偶数个
            slow = ReverseList.reverseList(slow);
        }
        while (slow != null) {
            if (head.val != slow.val) {
                return false;
            }
            slow = slow.next;
            head = head.next;
        }
        return true;
    }
}
