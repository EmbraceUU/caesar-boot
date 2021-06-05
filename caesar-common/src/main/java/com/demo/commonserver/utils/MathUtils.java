package com.demo.commonserver.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 面试过程中, 被问到如何在浮点型运算中防止精度缺失
 * 之前是碰到过这种问题的, 但是没有好好总结过
 * 如果没有BigDecimal类, 又如何做处理
 * 今天总结一下
 */
public class MathUtils {
    public static void main(String[] args) {
        int a = 345678;
        int b = 8989;
        System.out.println(a * b); // -1187667754 超出了Int的边界限制

        // int为32位, 正负为2^31次方
        System.out.println("int max " + Integer.MAX_VALUE);
        System.out.println("int min " + Integer.MIN_VALUE);

        // long为64位, 正负应该是2^63次方
        System.out.println("long max " + Long.MAX_VALUE);
        System.out.println("long min " + Long.MIN_VALUE);

        // float为32位
        System.out.println("float max " + Float.MAX_VALUE);
        System.out.println("float min " + Float.MIN_VALUE);

        // double为64位
        System.out.println("double max " + Double.MAX_VALUE);
        System.out.println("double min " + Double.MIN_VALUE);

        // 使用十进制来表示, 并使用scale来表示小数点位置来避免小数出现
        BigDecimal bigDecimal = new BigDecimal("1000.001");
        System.out.println(bigDecimal.scale());
        System.out.println(bigDecimal.toBigInteger());
        System.out.println(bigDecimal.unscaledValue());

        // BigInteger使用数组来避免long型溢出
        BigInteger bigInteger = new BigInteger("22222222222222");
        System.out.println(bigInteger.longValue());

    }
}
