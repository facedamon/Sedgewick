package org.example;

public class Feib {
    public static void main(String[] args) {
        try {
            System.out.println(getFeib(8));
            System.out.println(getFeib2(8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用循环实现，效率高
     *
     * @param i 第i个，从1开始
     * @return
     */
    private static int getFeib2(int i) {
        // 1 1 2 3 5 8 13 21
        // 0 1 2 3 4 5 6  7
        // f1=1;f2=1
        // f3=f1+f2 f3被保存下来
        //    |
        // f4=f3+f2 f4被保存下来
        //     ｜
        // f5=f4+f3 f5被保存下来
        if (i < 0) {
            throw new RuntimeException("i must gt 0");
        }
        if (i < 3) {
            return 1;
        }
        int f1 = 1;
        int f2 = 1;
        int fk = 0;

        while (i > 2) {
            fk = f1 + f2;
            f1 = f2;
            f2 = fk;
            i--;
        }
        return fk;
    }

    /**
     * 使用递归实现，效率低
     *
     * @param i 第i个，从1开始
     */
    private static int getFeib(int i) throws Exception {
        if (i < 0) {
            throw new RuntimeException("i must gt 0");
        }
        if (i < 3) {
            return 1;
        }
        return getFeib(i - 1) + getFeib(i - 2);
    }
}
