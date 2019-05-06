package com.demo.algorithms.reverseNumber;

/**
 * reverse number
 */
public class ReverseNumber {

    /**
     * revert func
     * @param num
     * @return
     */
    private static int revert(int num){

        int revNum = 0;
        while (num != 0){
            revNum = revNum * 10 + num % 10;
            num = num / 10;
        }
        return  revNum;
    }

    public static void main(String[] args) {
        System.out.println(revert(-1234));
    }
}
