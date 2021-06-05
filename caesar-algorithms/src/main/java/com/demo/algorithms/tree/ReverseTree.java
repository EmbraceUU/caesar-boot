package com.demo.algorithms.tree;

/**
 * revert tree
 */
public class ReverseTree {

    public TreeNode invertTree(TreeNode root) {

        if (root == null) {
            return null;
        }

        // 判断是否已经是根节点
        if (root.left == null && root.right == null) {
            return root;
        }

        // 递归反转树节点
        if (null != root.right) {
            invertTree(root.right);
        }
        // 递归反转书节点
        if (null != root.left) {
            invertTree(root.left);
        }

        // 反转逻辑
        TreeNode tempNode = new TreeNode(0);
        tempNode = root.left;
        root.left = root.right;
        root.right = tempNode;


        Class a = root.getClass();
        a.getDeclaredFields();


        return root;

    }
}

