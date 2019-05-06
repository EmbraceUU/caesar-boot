package com.demo.algorithms.stack;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Use queues to implement stacks
 */
public class StackDemo {
    /**
     * 标记当前节点位置
     */
    private int top;
    /**
     * 一开始用的List,但是很不方便 总是各种报错，然后用了LindedList链表实现
     */
    private Queue<Integer> queue;
    private Queue<Integer> queue1;

    /**
     * Initialize your data structure here.
     */
    public StackDemo() {
        // 初始化
        this.top = 0;
        this.queue = new LinkedList<Integer>();
        this.queue1 = new LinkedList<Integer>();
    }

    /**
     * 插入的时候，要保持顺序一致，将元素插入到非空的一队，如果都为空，就随机选择入队 。
     * Push element x onto stack.
     */
    private void push(int x) {
        // 判断两个队列是否为空
        if (!queue.isEmpty()) {
            queue.offer(x);
        } else {
            queue1.offer(x);
        }
        top++;
    }

    /**
     * 删除，要将（top-1）个元素插入到另外一个队列中。
     * 将剩余的最后一个元素删除。
     * Removes the element on top of the stack and returns that element.
     */
    private int pop() {
        int num = top - 1;
        top--;
        if (!queue.isEmpty()) {
            for (int i = 0; i < num; i++) {
                queue1.offer(queue.poll());
            }
            return queue.poll();
        } else {
            for (int i = 0; i < num; i++) {
                queue.offer(queue1.poll());
            }
            return queue1.poll();
        }
    }

    /**
     * Get the top element.
     */
    public int top() {
        // 获取栈顶元素
        int top_e = pop();
        push(top_e);
        return top_e;
    }

    /**
     * Returns whether the stack is empty.
     */
    public boolean empty() {
        // 直接返回标记
        return (queue.isEmpty() && queue1.isEmpty());
    }
}
