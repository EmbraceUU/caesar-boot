package com.demo.algorithms.search;

/**
 * 给定一个二维数组，其每一行从左到右递增排序，从上到下也是递增排序。
 * 给定一个数，判断这个数是否在该二维数组中。
 * Consider the following matrix:
 * [
 * [1,   4,  7, 11, 15],
 * [2,   5,  8, 12, 19],
 * [3,   6,  9, 16, 22],
 * [10, 13, 14, 17, 24],
 * [18, 21, 23, 26, 30]
 * ]
 */

public class MatrixSearch {
    /**
     * nums[0]  -> nums[M-1]
     * 如果小 往左  如果大了  往下  如果相等  退出
     * nums[i][N-1]  -> nums[i][0]
     */
    private static boolean matrix(int target, int[][] array) {
        int m = array.length;
        int n = array[0].length;
        for (int[] ints : array) {
            for (int j = n - 1; j >= 0; j--) {
                if (ints[j] == target) {
                    return true;
                } else if (ints[j] < target) {
                    break;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int target = 30;
        int[][] array = {
                {1,   4,  7, 11, 15},
                {2,   5,  8, 12, 19},
                {3,   6,  9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        };
        System.out.println(MatrixSearch.matrix(target, array));
    }
}
