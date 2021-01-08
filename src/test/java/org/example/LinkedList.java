package org.example;

import org.junit.Test;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList<Item>{

    private Node first;
    private Node last;
    private int N;

    public LinkedList(){}

    private class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;

        Node(Node<Item> prev, Item ele, Node<Item> next) {
            this.item = ele;
            this.next = next;
            this.prev = prev;
        }
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    // 向栈顶压入元素
    public void push(Item item) {
        Node<Item> oldFirst = first;
        Node<Item> newNode = new Node(null, item, oldFirst);
        first = newNode;
        if (oldFirst == null) {
            last = newNode;
        } else {
            oldFirst.prev = newNode;
        }
        N++;
    }

    // 从栈顶弹出元素
    public Item pop() {
        Node<Item> f = first;
        if (f == null) {
            throw new NoSuchElementException();
        } else {
            Item item = f.item;
            Node<Item> next = f.next;
            f.item = null;
            f.next = null;
            first = next;
            if (next == null) {
                last = null;
            } else {
                next.prev = null;
            }
            --N;
            return item;
        }
    }

    // 向队尾添加元素
    public void enqueue(Item item) {
        Node<Item> f = last;
        Node<Item> newNode = new Node<Item>(last, item, null);
        last = newNode;
        if (f == null) {
            first = newNode;
        } else {
            f.next = newNode;
        }
        ++N;
    }

    // 从队尾删除元素
    public Item dequeue() {
        Node<Item> f = last;
        if (f == null) {
            return null;
        } else {
            Node<Item> prev = f.prev;
            Item ele = f.item;
            f.item = null;
            f.prev = null;
            last = prev;
            if (prev == null) {
                first = null;
            } else {
                prev.next = null;
            }
            --N;
            return ele;
        }
    }

    Node<Item> node(int index) {
        Node x;
        int i;
        if (index < N >> 1) {
            x = first;
            for (i = 0; i < index; ++i) {
                x = x.next;
            }
            return x;
        } else {
            x = last;
            for (i = N - 1; i > index; --i) {
                x = x.prev;
            }
            return x;
        }
    }

    public boolean remove(Object o) {
        Node x;
        if (o == null) {
            for (x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    Item unlink(Node<Item> x) {
        Item ele = x.item;
        Node<Item> next = x.next;
        Node<Item> prev = x.prev;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }
        x.item = null;
        --N;
        return ele;
    }

    private class LinkedListIterator implements ListIterator<Item> {

        private Node<Item>  lastReturned;
        private Node<Item> next;
        private int nextIndex;

        public LinkedListIterator(int index) {
            this.next = (index == N) ? null : node(index);
            this.nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < N;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public Item previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            lastReturned = next = (next == null) ? last : next.prev;
            nextIndex--;
            return lastReturned.item;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
        }

        @Override
        public void set(Item item) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.item = item;
        }

        @Override
        public void add(Item item) {
        }
    }

    public ListIterator<Item> listIterator(int index) {
        return new LinkedListIterator(index);
    }

    @Test
    public void test() {
        LinkedList<Integer> linkedList = new LinkedList<Integer>();
        linkedList.push(1);
        linkedList.push(4);
        linkedList.push(0);

        ListIterator<Integer> iterable = linkedList.listIterator(0);
        while (iterable.hasNext()){
            System.out.println(iterable.next());
        }
        System.out.println(String.format("%s: %b\n", "isEmpty", linkedList.isEmpty()));
        System.out.println(String.format("%s: %d\n", "previous", iterable.previous()));

        linkedList.enqueue(6);
        linkedList.enqueue(19);
        linkedList.enqueue(13);
        linkedList.dequeue();
        linkedList.remove(6);

        iterable = linkedList.listIterator(linkedList.size());
        while (iterable.hasPrevious()){
            System.out.println(iterable.previous());
        }
        System.out.println(String.format("%s: %b\n", "isEmpty", linkedList.isEmpty()));
        System.out.println(String.format("%s: %d\n", "next", iterable.next()));
    }
}
