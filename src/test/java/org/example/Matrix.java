package org.example;

import org.junit.Test;

/**
 *
 */
public class Matrix {
    /**
     * 在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
     * 请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * </p>
     * 规律：首先选取数组中右上角的数字。如果该数字等于要查找的数字，查找过程结束：
     * 如果该数字大于要查找的数字，剔除这个数字所在的列：如果该数字小于要查找的数字，剔除这个数字所在的行。
     * 也就是说如果要查找的数字不在数组的右上角，则每－次都在数组的查找范围中剔除）行或者一列，这样每一步都可以缩小
     * 查找的范围，直到找到要查找的数字，或者查找范围为空。
     * <p>
     * 在前面的分析中，我们每一次都是选取数组查找范围的右上角数字。
     * 同样，我们也可以选取左下角的数字。
     * 但是我们不能选择左上角或者右下角。
     * 以左上角为例，最初数字1位于初始数组的左上角，由于1小于7，
     * 那么7应该位于1的右边或者下边。此时我们既不能从查找范围内提出1所在的行，
     * 也不能提出1所在的列，这样我们就无法缩小查找的范围。
     *
     * @param matrix 待查找的数组
     * @param number 要查找的数
     * @return 查找结果，true找到，false没有找到
     */
    public boolean find(int[][] matrix, int number) {
        //判断条件
        if (null == matrix || matrix.length < 1 || matrix[0].length < 1) {
            return false;
        }
        int rows = matrix.length;
        int cols = matrix[0].length;

        int row = 0;
        int col = cols - 1;

        while (row >= 0 && row < rows && col >= 0 && col < cols) {
            if (matrix[row][col] == number) {
                return true;
            } else if (matrix[row][col] > number) {
                --col;
            } else {
                ++row;
            }
        }
        return false;
    }

    @Test
    public void test() {
        int[][] matrix = {
                {1, 2, 8, 9},
                {2, 4, 9, 12},
                {4, 7, 10, 13},
                {6, 8, 11, 15}
        };
        System.out.println(find(matrix, 7));    // 要查找的数在数组中
        System.out.println(find(matrix, 5));    // 要查找的数不在数组中
        System.out.println(find(matrix, 1));    // 要查找的数是数组中最小的数字
        System.out.println(find(matrix, 15));   // 要查找的数是数组中最大的数字
        System.out.println(find(matrix, 0));    // 要查找的数比数组中最小的数字还小
        System.out.println(find(matrix, 16));   // 要查找的数比数组中最大的数字还大
        System.out.println(find(null, 16));     // 健壮性测试，输入空指针
    }
}
