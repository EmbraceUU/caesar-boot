package com.demo.algorithms.duplicate;

import java.util.HashMap;

/**
 * There are duplicate elements II
 */
public class ContainsNearbyDuplicate {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        // 一开始用双指针的方法，发现总是超时，在网上看到有人用HashMap，尝试一下
        HashMap<Integer, Integer> hm = new HashMap<>();
        // 一层循环
        for (int i = 0; i < nums.length; i++) {
            //如果map中存在当前值
            if (hm.containsKey(nums[i])) {
                // 当前指针和索引的差值
                int sub = i - hm.get(nums[i]);
                // 如果符合，返回true；不符合，将当前值作为key，当前指针作为value，存入map
                // 这种比较是：一直和前一个存在的重复值比较
                if (sub <= k)
                    return true;
                else
                    hm.put(nums[i], i);
            } else
                hm.put(nums[i], i);
        }
        return false;
    }
}
