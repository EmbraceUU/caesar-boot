package com.demo.algorithms.search;

import java.util.PriorityQueue;

/**
 * 二分查找
 * O(logn)
 */
public class BinarySearch {

    private static int binarySearch(int[] ints, int num){

        int low = 0;
        // 防止溢出, high应该是坐标
        int high = ints.length -1;
        int mid ;

        while (low <= high){

            mid = ( low + high ) / 2;

            if (num == ints[mid])
                return mid;

            if (num > ints[mid]) {
                low = mid + 1;
            }else{
                high = mid - 1;
            }

        }

        return -1;
    }

    public static void main(String[] args) {
        int[] ints = {-1, 0, 3, 5, 9, 12};
        int target = 13;
        binarySearch(ints, target);
    }

}
