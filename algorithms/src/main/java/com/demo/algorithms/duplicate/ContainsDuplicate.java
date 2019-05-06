package com.demo.algorithms.duplicate;

/**
 * There are duplicate elements
 */
public class ContainsDuplicate {
    public boolean containsDuplicate(int[] nums) {

        // 用两个指针解决
        // 第一层循环是当前需要比较的节点
        for (int i = 0; i < nums.length; i++) {
            // 第二层循环是和剩下的节点比较
            for (int j = (i + 1); j < nums.length; j++) {
                // 如果相同，返回true
                if (nums[i] == nums[j]) {
                    return true;
                }
            }
        }
        // 如果运行到了最后，就返回false;
        return false;
    }
}
