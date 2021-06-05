package com.demo.algorithms.power;

/**
 * check something is a power of 2
 */
public class PowerOfTwo {
    public boolean isPowerOfTwo(int n) {
        // 0不是2的幂，1是2的幂，负数不可能是
        // 用 & 按位与运算符：两位同时为1，结果才为1，否则为0
        if ((n > 0) && (n & (n - 1)) == 0) {
            return true;
        }
        return false;
    }
}
