package com.demo.algorithms.string;

public class LengthOfLongestSubstring {
    private static int lengthOfLongestSubstring(String s) {
        int head = 0, longest = 0;
        for (int i = 0 ; i < s.length() ; i++){
            for(int j = head; j < i; j++ ){
                if (s.charAt(j) == s.charAt(i)){
                    head = j + 1 ;
                    break;
                }
            }
            if (longest < i - head + 1){
                longest = i - head + 1;
            }
        }
        return longest;
    }

    public static void main(String[] args) {
        String s = "abcabcbbbqwer";
        System.out.println(lengthOfLongestSubstring(s));
    }
}
