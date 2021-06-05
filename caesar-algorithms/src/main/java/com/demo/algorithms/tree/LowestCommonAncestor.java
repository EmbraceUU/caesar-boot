package com.demo.algorithms.tree;

/**
 * Determine the nearest common parent of the binary tree
 */
public class LowestCommonAncestor {

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 如果有参数为null 直接返回Null
        if (root == null || p == null || q == null) {
            return null;
        }

        // 如果有一个节点是根节点 返回根节点
        if (p == root || q == root) {
            return root;
        }

        // 查找左子树的最近公共父节点
        TreeNode t_left = lowestCommonAncestor(root.left, p, q);
        // 查找右子树的最近公共节点
        TreeNode t_right = lowestCommonAncestor(root.right, p, q);

        // 如果两个公共父节点都不为NULL 判断为root
        // 如果左侧为NULL 判断两个节点没有在左侧 返回右子树返回值
        // 如果右侧为NULL 判断两个节点没有在右侧 返回左子树返回值
        if (t_left != null && t_right != null) {
            return root;
        } else if (t_left == null) {
            return t_right;
        } else {
            return t_left;
        }
    }
}
