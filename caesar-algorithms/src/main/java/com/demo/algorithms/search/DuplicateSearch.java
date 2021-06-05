package com.demo.algorithms.search;

/**
 * 算法1
 * 在一个长度为 n 的数组里的所有数字都在 0 到 n-1 的范围内。
 * 数组中某些数字是重复的，但不知道有几个数字是重复的，也不知道每个数字重复几次。请找出数组中任意一个重复的数字。
 * into: {2, 3, 1, 0, 2, 5}
 * out: 2
 */
public class DuplicateSearch {

    public boolean duplicate(int[] nums, int length, int[] duplication){

        if (nums == null || length == 0){
            return false;
        }

        for (int i = 0; i < length; i++){
            while (nums[i] != i){
                // 每一次用nums[i]做判断, 相等时跳出轮询
                // 用nums[i] 和 nums[nums[i]]做交换
                if (nums[i] == nums[nums[i]]){
                    duplication[0] = nums[i];
                    return true;
                }
                int temp = nums[i];
                nums[i] = nums[nums[i]];
                nums[nums[i]] = temp;
            }
        }

        return false;

    }
}
