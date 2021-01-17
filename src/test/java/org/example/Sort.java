package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * 介绍八大排序算法
 */
public class Sort {

    /**
     * 交换数组a中，位置i和位置j的数据
     *
     * @param a
     * @param i
     * @param j
     */
    private void swap(int[] a, int i, int j) {
        if (a == null || a.length < 1) {
            return;
        }
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * 打印数组
     *
     * @param a
     */
    private void printArr(int[] a) {
        if (a == null || a.length < 1) {
            return;
        }
        for (int k : a) {
            System.out.printf("%d\t", k);
        }
        System.out.println();
    }

    private long start = 0;

    @Before
    public void before() {
        this.start = System.currentTimeMillis();
    }

    @After
    public void after() {
        System.out.printf("运行时间:%d毫秒\n", (System.currentTimeMillis() - start));
    }

    /**
     * 冒泡排序
     * <p>
     * 已知一组无序数据a[1]、a[2]、...a[n]，需将其按升序排序。
     * 首先比较a[1]与a[2]的值，若a[1]大于a[2]则交换两者的值，否则不变。
     * 再比较a[2]与a[3]的值，若a[2]大于a[3]则交换两者的值，否则不变。
     * 再比较a[3]与a[4]，以此类推，最后比较a[n-1]与a[n]的值。
     * 这样处理一轮后，a[n]的值一定是这组数据中最大的。
     * 再对a[1]~a[n-1]以相同方法处理一轮，则a[n-1]的值就一定是a[1]~a[n-1]中最大的。
     * 再对a[1]~a[n-2]以相同方法处理一轮，以此类推。
     * 共处理n-1轮后数组a就是以升序排列了。
     * <p>
     * 总的来讲，每一轮排序后最大或者小的数将移动到序列的最后，**理论上总共要进行n(n-1)/2次交换**。
     *
     * @param a
     */
    public void bubbleSort(int[] a) {
        if (a == null || a.length < 1) {
            return;
        }
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                }
            }
        }
    }

    @Test
    public void testBubbleSort() {
        int[] a = {1, 2, 3, 4, 1, -1};
        bubbleSort(a);
        printArr(a);
    }

    /**
     * 一趟快速排序
     * <p>
     * 1. 设置两个变量i、j，排序开始的时候：i=0，j=N-1；
     * 2. 以第一个数组元素作为关键数据，赋值给key，即key=A[0]；
     * 3. 从j开始向前搜索，即由后开始向前搜索(j--)，找到第一个小于key的值A[j]，将A[j]和A[i]互换；
     * 4. 从i开始向后搜索，即由前开始向后搜索(i++)，找到第一个大于key的A[i]，将A[i]和A[j]互换；
     *
     * @param a
     * @param left
     * @param right
     * @return
     */
    private int partition(int[] a, int left, int right) {
        if (a == null || a.length < 1 || left < 0 || left > right) {
            return -1;
        }
        int x = a[left];
        int i = left;
        int j = right;
        while (i < j) {
            while (i < j && a[j] > x) {
                j--;
            }
            if (i < j) {
                a[i++] = a[j];
            }
            while (i < j && a[i] < x) {
                i++;
            }
            if (i < j) {
                a[j--] = a[i];
            }
        }
        a[i] = x;
        return i;
    }

    /**
     * 按照同样的方法，对子数列进行递归遍历。最后得到有序数组
     *
     * @param a
     * @param left
     * @param right
     */
    public void quickSort(int[] a, int left, int right) {
        if (a == null || a.length < 1 || a.length < right || left < 0 || left > right) {
            throw new RuntimeException("quickSort param err.");
        }
        int k = partition(a, left, right);
        if (k > left) {
            quickSort(a, left, k - 1);
        }
        if (k < right) {
            quickSort(a, k + 1, right);
        }
    }

    @Test
    public void testQuickSort() {
        int[] a = {1, 2, 3, 4, 1, -1};
        quickSort(a, 0, a.length - 1);
        printArr(a);
    }

    /**
     * 非递归
     *
     * @param a
     * @param left
     * @param right
     */
    public void quickSort2(int[] a, int left, int right) {
        if (a == null || a.length < 1 || a.length < right || left < 0 || left > right) {
            throw new RuntimeException("quickSort param err.");
        }
        Deque<Integer> stack = new ArrayDeque<Integer>();
        // 先押入右下标，再押入左下标
        stack.push(right);
        stack.push(left);
        // i表示left，j表示right
        int i, j;
        while (!stack.isEmpty()) {
            i = stack.pop();
            j = stack.pop();
            if (i < j) {
                int k = partition(a, i, j);
                if (k > i) {
                    // 如果k > i，说明k的左侧都是比自身小的，则排序左侧
                    // 同样先押入右下标，再押入左下标
                    stack.push(k - 1);
                    stack.push(i);
                }
                if (k < j) {
                    stack.push(j);
                    stack.push(k + 1);
                }
            }
        }
    }

    @Test
    public void testQuickSort2() {
        int[] a = {1, 2, 3, 4, 1, -1};
        quickSort2(a, 0, a.length - 1);
        printArr(a);
    }

    /**
     * 直接插入排序(Straight Insertion Sort)的基本思想是：
     * 把n个待排序的元素看成为一个有序表和一个无序表。
     * 开始时有序表中只包含1个元素，无序表中包含有n-1个元素，
     * 排序过程中每次从无序表中取出第一个元素，将它插入到有序表中的适当位置，使之成为新的有序表，重复n-1次可完成排序过程。
     *
     * @param a
     */
    public void insertSort(int[] a) {
        if (a == null || a.length < 1) {
            return;
        }
        int i, j, k;
        int n = a.length;
        for (i = 1; i < n; i++) {
            // 为a[i]在前面的a[0...n-1]有序区间中找一个合适的位置j
            for (j = i - 1; j >= 0; j--) {
                if (a[j] < a[i]) {
                    break;
                }
            }
            //找到了一个合适的位置j
            if (j != i - 1) {
                //将比a[i]大的数据向后移动一位，腾出j的位置
                int temp = a[i];
                for (k = i - 1; k > j; k--) {
                    a[k + 1] = a[k];
                }
                //当循环结束时k=j
                //将a[i]放入k+1的位置
                a[k + 1] = temp;
            }
        }
    }

    @Test
    public void testInsertSort() {
        int[] a = {1, 2, 3, 4, 1, -1};
        insertSort(a);
        printArr(a);
    }

    /**
     * 𝐷𝑘=2^𝑘−1
     * ：{1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191...}
     * Hibbard增量
     *
     * @param len
     * @return
     */
    private List<Integer> getHibbard(int len) {
        if (len < 1) {
            return null;
        }
        List<Integer> arr = new ArrayList<>();
        int k = 1;
        while (true) {
            int temp = (1 << k++) - 1;
            if (temp <= len) {
                arr.add(temp);
            } else {
                break;
            }
        }
        return arr;
    }

    /**
     * 对希尔排序中的单组进行直接插入排序
     *
     * @param a    待排序的数组
     * @param left 数组的起始位置
     * @param gap  步长
     */
    private void gapSort(int[] a, int left, int gap) {
        if (a == null || a.length < 1 || a.length < left) {
            return;
        }
        int len = a.length;
        for (int j = left + gap; j < len; j += gap) {
            if (a[j] < a[j - gap]) {
                int temp = a[j];
                int k = j - gap;
                while (k >= 0 && a[k] > temp) {
                    a[k + gap] = a[k];
                    k -= gap;
                }
                a[k + gap] = temp;
            }
        }
    }

    public void shellSort(int[] a) {
        if (a == null || a.length < 1) {
            return;
        }
        int len = a.length;
        List<Integer> gaps = getHibbard(len);
        ListIterator<Integer> lt = gaps.listIterator(gaps.size());
        while (lt.hasPrevious()) {
            Integer gap = lt.previous();
            // 共gap个组，对每一组都执行直接插入排序
            for (int i = 0; i < gap; i++) {
                gapSort(a, i, gap);
            }
        }
    }

    @Test
    public void testShellSort() {
        int[] a = {1, 2, 3, 4, 1, -1};
        //         0  1  2  3  4   5
        //{1,4}{2,1}{3,-1}->{1,4}{1,2}{-1,3}->1,1,-1,4,2,3
        //{1,1,-1,4,2,3}
        shellSort(a);
        printArr(a);
    }

    /**
     * 简单选择排序
     *
     * @param a
     */
    public void selectSort(int[] a) {
        if (a == null || a.length < 1) {
            return;
        }
        // 有序区间的末尾
        int i;
        // 无序区间的起始
        int j;
        // 无序区间中最小元素
        int min;
        int len = a.length;
        for (i = 0; i < len; i++) {
            min = i;
            //找出a[i+1]~a[n]之间的最小元素
            for (j = i + 1; j < len; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            // 若min != i, 说明无序区间存在小元素
            // 交换之后，保证了a[0]~a[i]之间的元素是有序的
            if (min != i) {
                swap(a, i, min);
            }
        }
    }

    @Test
    public void testSelectSort() {
        int[] a = {1, 2, 3, 4, 1, -1};
        selectSort(a);
        printArr(a);
    }

    /**
     * 将一个数组中的两个相邻有序区间合并成一个
     *
     * @param a     包含两个有序区间
     * @param left  第一个有序区间的起始下标
     * @param mid   第一个有序区间的结束下标，也是第二个有序区间的起始下标
     * @param right 第二个有序区间的结束下标
     */
    private void merge(int[] a, int left, int mid, int right) {
        if (a == null || a.length < 1 || left < 0 || right >= a.length) {
            return;
        }
        //汇总2个有序区间的临时区域
        int[] temp = new int[right - left + 1];
        //第1个有序区间索引
        int i = left;
        //第2个有序区间索引
        int j = mid + 1;
        //临时区间索引
        int k = 0;
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = a[i++];
        }
        while (j <= right) {
            temp[k++] = a[j++];
        }
        //将排序后的元素全部整合到数组a中
        for (i = 0; i < k; i++) {
            a[left + i] = temp[i];
        }
        temp = null;
    }

    /**
     * 对数组a做若干次合并：
     * 数组a的总长度为len，将它分为若干个长度为gap的子数组
     * 将每2个相邻的子数组进行合并排序
     *
     * @param a
     * @param gap
     */
    private void mergeGroups(int[] a, int gap) {
        if (a == null || a.length < 1 || gap > a.length) {
            return;
        }
        int i;
        int k = gap << 1;
        int len = a.length;
        //将每2个相邻的子数组进行合并排序
        for (i = 0; i + k < len; i += k) {
            merge(a, i, i + gap - 1, i + k - 1);
        }
        // 剩余一个子数组没有被匹配
        if (i + gap - 1 < len - 1) {
            merge(a, i, i + gap - 1, len - 1);
        }
    }

    public void mergeSort(int[] a) {
        if (a == null || a.length < 1) {
            return;
        }
        int len = a.length;
        for (int i = 1; i < len; i <<= 1) {
            mergeGroups(a, i);
        }
    }

    @Test
    public void testMergeSort() {
        int[] a = {1, 2, 3, 4, 1, -1};
        mergeSort(a);
        printArr(a);
    }

    private int getMaxValue(int[] a) {
        if (a == null || a.length < 1) {
            return -1;
        }
        int max = a[0];
        for (int v : a) {
            if (v < 0) {
                throw new IllegalArgumentException("number must grant than zero");
            }
            if (max < v) {
                max = v;
            }
        }
        return max;
    }

    /**
     * 计数排序
     *
     * @param a
     */
    public void countSort(int[] a) {
        if (a == null || a.length < 1) {
            return;
        }
        int tempLen = getMaxValue(a) + 1;
        int[] temp = new int[tempLen];
        for (int v : a) {
            temp[v]++;
        }
        int i = 0;
        for (int j = 0; j < tempLen; j++) {
            while (temp[j] > 0) {
                a[i++] = j;
                temp[j]--;
            }
        }
    }

    @Test
    public void testCountSort() {
        int[] a = {1, 3, 2, 1, 4};
        countSort(a);
        printArr(a);
    }

    /**
     * 桶排序
     *
     * @param a
     * @param bucketSize 桶数
     */
    public void bucketSort(int[] a, int bucketSize) {
        if (a == null || a.length < 1 || bucketSize < 1) {
            return;
        }
        int min;
        int max;
        min = max = a[0];
        for (int v : a) {
            if (v < min) {
                min = v;
            } else if (v > max) {
                max = v;
            }
        }
        // 每个桶装载的数据量
        int bucketCount = (int) (Math.floor(max - min) / bucketSize) + 1;
        // 二维数组，一位表示桶，二位表示桶中数据
        int[][] buckets = new int[bucketCount][0];
        // 利用映射函数将数据分配到各个桶中
        int len = a.length;
        for (int i = 0; i < len; i++) {
            int index = (int) (Math.floor(a[i] - min) / bucketSize);
            buckets[index] = append(buckets[index], a[i]);
        }
        int index = 0;
        for (int[] bucket : buckets) {
            if (bucket.length <= 0) {
                continue;
            }
            // 每个桶中数据进行插入排序
            insertSort(bucket);
            for (int v : bucket) {
                a[index++] = v;
            }
        }
    }

    /**
     * 扩容
     *
     * @param bucket
     * @param i
     * @return
     */
    private int[] append(int[] bucket, int i) {
        bucket = Arrays.copyOf(bucket, bucket.length + 1);
        bucket[bucket.length - 1] = i;
        return bucket;
    }

    @Test
    public void testBucketSort() {
        int[] a = {1, 3, 2, 1, 4};
        bucketSort(a, 3);
        printArr(a);
    }
}
