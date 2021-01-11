package org.example;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * 用两个栈模拟实现队列
 * 分别完成在队列尾部插入结点和在队列头部删除结点的功能。
 * @param <T>
 */
public class Queue<T> {
    private Deque<T> deque1 = new ArrayDeque<>();
    private Deque<T> deque2 = new ArrayDeque<>();

    public void appendTail(T t) {
        deque1.push(t);
    }

    public T deleteHead() {
        if (deque2.isEmpty()) {
            while (!deque1.isEmpty()) {
                deque2.push(deque1.pop());
            }
        }
        if (deque2.isEmpty()) {
            throw new RuntimeException("No more element");
        }
        return deque2.pop();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (!deque2.isEmpty()) {
            Iterator lt = deque2.iterator();
            while (lt.hasNext()) {
                sb.append(lt.next()).append(",");
            }
        }
        if (!deque1.isEmpty()) {
            Iterator lt = deque1.descendingIterator();
            while (lt.hasNext()) {
                sb.append(lt.next()).append(",");
            }
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     *
     */
    @Test
    public void test1() {
        Queue<Integer> queue = new Queue<Integer>();
        queue.appendTail(1);
        queue.appendTail(2);
        queue.appendTail(3);
        queue.appendTail(4);
        queue.deleteHead();
        System.out.println(queue.toString());
        queue.deleteHead();
        queue.deleteHead();
        queue.deleteHead();
        System.out.println(queue.toString());
        queue.deleteHead();
    }
}
