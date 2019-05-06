package com.demo.algorithms.queue;

import java.util.Stack;

/**
 * Implement queues with stacks
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */
public class QueueDemo {
    private Stack s1;
    private Stack s2;

    /**
     * Initialize your data structure here.
     */
    public QueueDemo() {
        this.s1 = new Stack();
        this.s2 = new Stack();
    }

    /**
     * Push element x to the back of queue.
     */
    public void push(int x) {
        // 进堆，直接存入s1
        s1.push(x);
    }

    /**
     * Removes the element from in front of queue and returns that element.
     */
    public int pop() {
        // 将s1的元素依次入s2，出栈顶元素
        while (!s1.empty()) {
            s2.push(s1.pop());
        }
        // 返回栈顶元素
        int x = (int) s2.pop();
        while (!s2.empty()) {
            s1.push(s2.pop());
        }
        return x;
    }

    /**
     * Get the front element.
     */
    public int peek() {
        // 将s1的元素依次入s2，出栈顶元素
        while (!s1.empty()) {
            s2.push(s1.pop());
        }
        // 返回栈顶元素
        int x = (int) s2.peek();
        while (!s2.empty()) {
            s1.push(s2.pop());
        }
        return x;
    }

    /**
     * Returns whether the queue is empty.
     */
    public boolean empty() {
        if (s1.empty() && s2.empty()) {
            return true;
        } else {
            return false;
        }
    }
}
