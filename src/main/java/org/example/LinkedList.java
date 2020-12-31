package org.example;

import java.util.Iterator;

public class LinkedList<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int N;

    private class Node {
        Item item;
        Node next;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    // 向栈顶压入元素
    public void push(Item item) {
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        N++;
    }

    // 从栈顶弹出元素
    public Item pop() {
        Item item = first.item;
        first = first.next;
        N--;
        return item;
    }

    // 向队尾添加元素
    public void enqueue(Item item) {
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
        }
        N++;
    }

    // 从队头删除元素
    public Item dequeue() {
        Item item = first.item;
        first = first.next;
        if (isEmpty()) {
            last = null;
        }
        N--;
        return item;
    }

    private class LinkedListIterator<Item> implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return N != 0;
        }

        @Override
        public Item next() {
            Item item = (Item) current.item;
            current = current.next;
            N--;
            return item;
        }

        @Override
        public void remove() {
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedListIterator();
    }

    public static void main(String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<Integer>();
        linkedList.push(1);
        linkedList.push(4);
        linkedList.push(0);

        Iterator<Integer> iterable = linkedList.iterator();
        while (iterable.hasNext()){
            System.out.println(iterable.next());
        }
        System.out.println(linkedList.isEmpty());

        linkedList.enqueue(6);
        linkedList.enqueue(19);
        linkedList.enqueue(13);
        linkedList.dequeue();

        iterable = linkedList.iterator();
        while (iterable.hasNext()){
            System.out.println(iterable.next());
        }
        System.out.println(linkedList.isEmpty());

    }
}
