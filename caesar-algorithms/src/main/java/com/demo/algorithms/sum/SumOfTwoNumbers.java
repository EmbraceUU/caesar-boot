package com.demo.algorithms.sum;

/**
 * the sum of two numbers
 */
public class SumOfTwoNumbers {

    /**
     * sum func
     * @param nums
     * @param target
     * @return
     */
    public int[] sum(int[] nums, int target) {
        int[] res = {0, 0};
        //第一层循环是从0开始到结尾，一共四遍循环
        for (int i = 0; i < nums.length; i++) {
            //里面这层循环就是从当前下标开始，循环到结尾
            //避免了会两者重复
            for (int j = (i + 1); j < nums.length; j++) {
                //求两者之和
                int sum = nums[i] + nums[j];
                //判断是否相同，相同即推出循环
                if (sum == target) {
                    res[0] = i;
                    res[1] = j;
                    break;
                }
            }
        }
        return res;
    }
}
