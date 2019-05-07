package com.demo.algorithms.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolveNQueens {

    public List<List<String>> solveNQueens(int n) {
        int rowIndex = 0;
        List<List<String>> res = new ArrayList<>();
        boolean[] row = new boolean[n]; // row 记忆
        boolean[] left = new boolean[2 * n]; // 左下角 记忆
        boolean[] right = new boolean[2 * n]; // 右下角 记忆
        backtrack(res, new ArrayList<>(), row, left, right, rowIndex, n);
        return res;
    }

    private void backtrack(List<List<String>> res, ArrayList<String> temp, boolean[] row, boolean[] left, boolean[] right, int rowIndex, int n) {
        if (rowIndex == n) {
            res.add(new ArrayList<>(temp));
            return;
        }
        for (int i = 0; i < n; i++) {
            if (row[i] || left[i + rowIndex] || right[rowIndex - i + n - 1]) {
                continue;
            }

            row[i] = true;
            left[i + rowIndex] = true;
            right[rowIndex - i + n - 1] = true;

            char[] tempString = new char[n];
            Arrays.fill(tempString, '.');
            tempString[i] = 'Q';
            String string = "";
            for (char c : tempString) {
                string += c + "";
            }
            temp.add(string);
            backtrack(res, temp, row, left, right, rowIndex + 1, n);
            row[i] = false;
            left[i + rowIndex] = false;
            right[rowIndex - i + n - 1] = false;
            temp.remove(temp.size() - 1);
        }
    }
}
