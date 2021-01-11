package org.example;

import org.junit.Test;

import java.util.*;

public class BinaryTreeBuild {
    private static class BinaryTreeNode {
        int value;
        BinaryTreeNode left;
        BinaryTreeNode right;

        public BinaryTreeNode(int value, BinaryTreeNode left, BinaryTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 输入某二叉树的前序遍历和中序遍历的结果，请重建出该二节树。
     * 假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
     *
     * @param preorder 前序遍历
     * @param inorder 中序遍历
     * @return 根节点
     */
    public BinaryTreeNode construct(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null || preorder.length != inorder.length || inorder.length < 1) {
            return null;
        }
        return constructCore(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }

    /**
     * 在二叉树的前序遍历中，第一个数字总是树的根节点的值。但在中序遍历中，根节点的值在序列的中间，左子树
     * 的节点的值位于根节点的左边，而右子树的节点位于根节点的右边。因此我们需要扫描中序遍历序列，才能找到
     * 根节点的值。
     *
     * @example preorder 1 2 4 7 3 5 6 8
     * @example inorder  4 7 2 1 5 3 8 6
     *
     * 前序遍历的第一个数字1就是根节点，在中序遍历中1前面的3个数字都是左子树，位于1后面的数字都是右子树
     * 左子树有3个左子节点，同样，在前序遍历中，根节点后面的3个数字就是3个左子树节点，再后面的所有数字都是
     * 右子树节点。这样我们就在前序遍历和中序遍历两个序列中分别找到了左右子树
     *
     * 既然已经分别找到了左右子树的前序序列和中序序列，我们可以用同样的方法分别去构建左右子树。
     * 也就是说，接下来的事情可以用递归的方法去完成。
     *
     * @param preorder 前序遍历
     * @param ps 前序遍历开始位置
     * @param pe 前序遍历结束位置
     * @param inorder  中续遍历
     * @param is 中续遍历开始位置
     * @param ie 中续遍历结束位置
     * @return 根节点
     */
    private BinaryTreeNode constructCore(int[] preorder, int ps, int pe, int[] inorder, int is, int ie) {
        // 开始位置大于结束位置说明已经没有需要处理的元素了
        if (ps > pe) {
            return null;
        }
        // 取前序遍历的第一个数字，就是当前的根节点
        int value = preorder[ps];
        // 在中序遍历中找到根节点的位置
        int index = is;
        while (index <= ie && inorder[index] != value) {
            index++;
        }
        // 如果在整个中序遍历的数组中没有找到，说明输入参数不合法
        if (index > ie) {
            throw new RuntimeException("Invalid input");
        }
        // 创建当前的根节点
        BinaryTreeNode node = new BinaryTreeNode(value, null, null);

        // 递归创建当前根节点的左子树，左子树的元素个数：index-is+1个
        node.left = constructCore(preorder, ps + 1, ps + index - is, inorder, is, index - 1);
        // 递归创建当前根节点的右子树，右子树的元素个数：ie-index个
        node.right = constructCore(preorder, ps + index - is + 1, pe, inorder, index + 1, ie);
        return node;
    }

    public void printTree(BinaryTreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<BinaryTreeNode> stack = new ArrayDeque<>();
        BinaryTreeNode node = root;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            if (!stack.isEmpty()) {
                node = stack.pop();
                list.add(node.value);
                node = node.right;
            }
        }
        System.out.println(list.toString());
    }

    /**
     * 普通二叉树
     *              1
     *            /    \
     *           2      3
     *          /      / \
     *         4      5   6
     *          \        /
     *           7      8
     */
    @Test
    public void test1() {
        int[] preorder = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] inorder = {4, 7, 2, 1, 5, 3, 8, 6};
        printTree(construct(preorder, inorder));
    }

    /**
     * 所有结点都没有右子结点
     *              1
     *             /
     *            2
     *           /
     *          3
     *         /
     *        4
     *       /
     *      5
     */
    @Test
    public void test2() {
        int[] preorder = {1, 2, 3, 4, 5};
        int[] inorder = {5, 4, 3, 2, 1};
        printTree(construct(preorder, inorder));
    }

    /**
     * 所有结点都没有左子结点
     *              1
     *               \
     *                2
     *                 \
     *                  3
     *                   \
     *                    4
     *                     \
     *                      5
     */
    @Test
    public void test3() {
        int[] preorder = {1, 2, 3, 4, 5};
        int[] inorder = {1, 2, 3, 4, 5};
        printTree(construct(preorder, inorder));
    }

    /**
     * 只有一个节点
     */
    @Test
    public void test4() {
        int[] preorder = {1};
        int[] inorder = {1};
        printTree(construct(preorder, inorder));
    }

    /**
     * 完全二叉树
     *              1
     *             / \
     *            2   3
     *          /  \ /  \
     *         4  5 6   7
     */
    @Test
    public void test5() {
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder = {4, 2, 5, 1, 6, 3, 7};
        printTree(construct(preorder, inorder));
    }

    /**
     * 空指针
     */
    @Test
    public void test6() {
        construct(null, null);
    }

    /**
     * 输入的两个序列不匹配
     */
    @Test
    public void test7() {
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder = {4, 2, 8, 1, 6, 3, 7};
        printTree(construct(preorder, inorder));
    }
}
