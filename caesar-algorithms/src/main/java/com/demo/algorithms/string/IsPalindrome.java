package com.demo.algorithms.string;

/**
 * 回文字符串
 * 输入: "A man, a plan, a canal: Panama"
 * 输出: true
 * 解释: "amanaplanacanalpanama"
 *
 * 输入: "race a car"
 * 输出: false
 * 解释: "raceacar"
 */
public class IsPalindrome {

    public static boolean isPalindrome(String str){
        // 需要判空
        if (str == null || str.length() == 0) return true;

        // 声明两个指针指向头和尾
        int front , end;
        front = 0;
        end = str.length() - 1;

        // 遍历整个字符串
        while (front < end){

            // 过滤掉符号和空格
            while (front < str.length() && isValid(str.charAt(front))){
                front ++ ;
            }

            // 判断是否全部是空格和符号
            // 应该是等于length
            // 必须判断这一步, 否则下面end无法控制, 会报越界异常
            if (front == str.length()) return true;

            // 从后面过滤符号和空格
            while (end >= 0 && isValid(str.charAt(end))){
                end --;
            }

            if (str.toLowerCase().charAt(front) != str.toLowerCase().charAt(end)){
                break;
            }else {
                front ++;
                end --;
            }

        }
        return end <= front;
    }

    // 判断是否是数字或者字母
    private static boolean isValid(char c){
        return !Character.isLetter(c) && !Character.isDigit(c);
    }

    public static void main(String[] args) {
        String string = ".,";
        System.out.println(IsPalindrome.isPalindrome(string));
    }
}
