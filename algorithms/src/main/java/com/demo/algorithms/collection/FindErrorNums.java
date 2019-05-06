package com.demo.algorithms.collection;

/**
 * 错误的集合
 */
public class FindErrorNums {

    private static int[] findErrorNums(int[] nums) {
        int[] array = new int[nums.length + 1];
        for (int i : nums) {
            array[i]++;
        }
        int[] result = new int[2];
        for (int i = 1; i < array.length; i++) {
            // nums中出现两次的数就是array中2的下标
            if (array[i] == 2) {
                result[0] = i;
            }
            // nums丢失的数就是array中0的下标
            if (array[i] == 0) {
                result[1] = i;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 2, 4};
        nums = findErrorNums(nums);
        System.out.println(nums);
    }
}
