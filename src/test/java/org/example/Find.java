package org.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 介绍常用查找算法
 */
public class Find {

    /**
     * 顺序查找也称为线形查找，属于无序查找算法。
     * 从数据结构线形表的一端开始，顺序扫描，依次将扫描到的结点关键字与给定值k相比较，
     * 若相等则表示查找成功；若扫描结束仍没有找到关键字等于k的结点，表示查找失败。
     * @param a
     * @param value
     * @param n
     * @return
     */
    int sequenceFind(int[] a, int value, int n) {
        if (a == null || a.length != n || n < 1) {
            return -1;
        }
        if (n == 1 && a[0] != value) {
            return -1;
        }
        for (int i = 0; i < n; i++) {
            if (a[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 二分查找也称为是折半查找，属于有序查找算法。
     * 用给定值k先与中间结点的关键字比较，中间结点把线形表分成两个子表，
     * 若相等则查找成功；若不相等，再根据k与该中间结点关键字的比较结果确定下一步查找哪个子表，
     * 这样递归进行，直到查找到或查找结束发现表中没有这样的结点。
     * @param a
     * @param value
     * @param n
     * @return
     */
    int binaryFind(int[] a, int value, int n) {
        if (a == null || a.length != n || n < 1) {
            return -1;
        }
        if (n == 1 && a[0] != value) {
            return -1;
        }
        int low = 0;
        int high = n - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) >> 1;
            if (a[mid] == value) {
                return mid;
            }
            if (a[mid] > value) {
                high = mid - 1;
            }
            if (a[mid] < value) {
                low = mid + 1;
            }
        }
        return -1;
    }

    @Test
    public void testBinaryFind() {
        int[] a = {1, 2, 3, 4, 5, 6};
        //int[] a = {};
        //int[] a = {1};
        //int[] a = {1,2};
        //int[] a = {1,2, 4};
        System.out.println(binaryFind(a, 3, a.length));
    }

    // 二分查找 递归
    int binaryFindRev(int[] a, int value, int low, int high) {
        if (a == null || a.length < 1) {
            return -1;
        }
        int mid = low + ((high - low) >> 1);
        if (a[mid] == value) {
            return mid;
        }
        if (mid >= high) {
            return -1;
        }
        if (a[mid] > value) {
            return binaryFindRev(a, value, low, mid - 1);
        }
        if (a[mid] < value) {
            return binaryFindRev(a, value, mid + 1, high);
        }
        return -1;
    }

    @Test
    public void testBinaryFindRev() {
        int[] a = {1, 2, 3, 4, 5, 6};
        //int[] a = {};
        //int[] a = {1};
        //int[] a = {1,2};
        System.out.println(binaryFindRev(a, 3, 0, a.length - 1));
    }

    /**
     * 插值查找：
     * mid = (low + high) / 2; ==> mid = low + 1/2 * (high - low);
     * mid = low + (key - a[low])/(a[high] - a[low]) * (high - low);
     * 基于二分查找算法，将查找点的选择改进为自适应选择
     * @param a
     * @param value
     * @param n
     * @return
     */
    int insertionFind(int[] a, int value, int n) {
        if (a == null || a.length != n || n < 1) {
            return -1;
        }
        if (n == 1 && a[0] != value) {
            return -1;
        }
        int low = 0;
        int high = n - 1;
        int mid = 0;
        while (low <= high) {
            // 防止by zero 错误
            if (low == high) {
                if (a[low] == value) {
                    return low;
                }
                return -1;
            }
            mid = low + (value - a[low])/(a[high] - a[low]) * (high - low);
            if (a[mid] == value) {
                return mid;
            }
            if (a[mid] > value) {
                high = mid - 1;
            }
            if (a[mid] < value) {
                low = mid + 1;
            }
        }
        return -1;
    }

    @Test
    public void testInsertionFind() {
        int[] a = {1, 2, 3, 4, 5, 6};
        //int[] a = {};
        //int[] a = {1};
        //int[] a = {1,2,3};
        //int[] a = {1,2,4};
        System.out.println(insertionFind(a, 3, a.length));
    }

    // 斐波那契数组的长度
    private static final int MAX_SIZE = 20;

    // 构造一个斐波那契数组
    int[] fibonacci() {
        int[] f = new int[MAX_SIZE];
        f[0] = 0;
        f[1] = 1;
        for (int i = 2; i < MAX_SIZE; i++) {
            f[i] = f[i-1]+f[i-2];
        }
        return f;
    }

    // 斐波那契查找
    int fibonacciFind(int a[], int value) {
        if (a == null || a.length < 1) {
            return -1;
        }
        int low = 0;
        int high = a.length - 1;
        // fibonacci index
        int k = 0;
        int mid = 0;
        int[] f = fibonacci();
        while (f[k] - 1 < a.length) {
            k++;
        }
        // 扩展到f[k]长度
        int[] temp = Arrays.copyOf(a, f[k]);
        // 扩展的空间复制最后一位的数字
        for (int i = high+1; i < temp.length; i++) {
            temp[i] = a[high];
        }

        while (low <= high) {
            mid = low + f[k-1]-1;
            if (temp[mid] > value) {
                high = mid - 1;
                k--;
            } else if (temp[mid] < value) {
                low = mid + 1;
                k-=2;
            } else {
                //查找值的下标在arr数组额范围内，直接返回
                if (mid < a.length) {
                    return mid;
                } else {
                    //不在就返回right,为什么？因为后面几位的值和right的值是一样的，说明查找的值就是right
                    return high;
                }
            }
        }
        return -1;
    }

    @Test
    public void testFibonacciFind() {
        int[] a = {0,16,24,35,47,59,62,88,99};
        //int[] a = {0,16,24,35,47,59,62,88};
        //int[] a = {0,16};
        //int[] a = {0};
        //int[] a = {99};
        //int[] a = {};
        System.out.println(fibonacciFind(a, 99));
    }

    /**
     * 将n个数据元素"按块有序"划分为m块（m ≤ n）。
     * 每一块中的结点不必有序，但块与块之间必须"按块有序"；
     * 即第1块中任一元素的关键字都必须小于第2块中任一元素的关键字；
     * 而第2块中任一元素又都必须小于第3块中的任一元素，……
     *
     * 1. 先选取各块中的最大关键字构成一个索引表
     * 2. 查找分两个部分：先对索引表进行二分查找或者顺序查找，以确定待查记录在哪一块中；然后，在已确定的块中用顺序法进行查找
     */
    private static class BlockFind {
        // 索引表
        private int[] index;
        // 块
        private ArrayList<Integer>[] lists;

        // 构建索引表
        public BlockFind(int[] index) {
            if (index != null && index.length != 0) {
                this.index = index;
                this.lists = new ArrayList[index.length];
                for (int i = 0; i < lists.length; i++) {
                    lists[i] = new ArrayList<Integer>();
                }
            } else {
                throw new RuntimeException("index cannot be null or empty.");
            }
        }

        /**
         * 二分查找定位索引位置
         * @param data 要插入的值
         * @return
         */
        private int binaryFind(int data) {
            int low = 0;
            int high = index.length - 1;
            int mid = 0;
            while (low <= high) {
                mid = (low + high) >> 1;
                if (index[mid] > data) {
                    high = mid - 1;
                } else {
                    // 如果相等，也插入在后面
                    low = mid + 1;
                }
            }
            return low;
        }

        // 打印每块元素
        public void printAll() {
            for (int i = 0; i < lists.length; i++) {
                ArrayList list = lists[i];
                System.out.println("ArrayList " + i + ":");
                System.out.println(lists[i].toString());
            }
        }

        // 插入元素
        public void insert(int value) {
            int i = binaryFind(value);
            lists[i].add(value);
        }

        // 查找元素
        public String find(int data) {
            int i = binaryFind(data);
            for (int j = 0; j < lists[i].size(); j++) {
                if (data == lists[i].get(j)) {
                    return String.format("在第%d块，第%d个",i+1, j+1);
                }
            }
            return String.format("%d 不存在", data);
        }
    }

    @Test
    public void testBlockFind() {
        int[] index = {10, 20, 30};
        BlockFind blockFind = new BlockFind(index);
        blockFind.insert(1);
        blockFind.insert(12);
        blockFind.insert(22);

        blockFind.insert(9);
        blockFind.insert(18);
        blockFind.insert(23);

        blockFind.insert(5);
        blockFind.insert(15);
        blockFind.insert(27);

        blockFind.printAll();

        System.out.println(blockFind.find(18));
        System.out.println(blockFind.find(29));
    }
}
