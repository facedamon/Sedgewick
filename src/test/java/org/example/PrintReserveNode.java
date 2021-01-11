package org.example;

import org.junit.Test;

import java.util.Stack;

public class PrintReserveNode {
    private static class ListNode {
        int val;
        ListNode next;

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * 不改变链表指针，从尾到头打印出每个节点的值
     * 使用栈的方式
     * 如果使用递归，性能会有问题
     * @param node
     */
    private void printReserveNodes(ListNode node) {
        Stack<ListNode> stack = new Stack<>();
        while (node != null) {
            stack.push(node);
            node = node.next;
        }
        while (!stack.isEmpty()) {
            System.out.print(stack.pop().val + "\t");
        }
    }

    @Test
    public void test() {
        ListNode root = new ListNode(1, null);
        ListNode two = new ListNode(2, null);
        root.next = two;
        ListNode three = new ListNode(3, null);
        two.next = three;
        ListNode four = new ListNode(4, null);
        three.next = four;
        ListNode five = new ListNode(5, null);
        four.next = five;

        printReserveNodes(root);
    }
}
