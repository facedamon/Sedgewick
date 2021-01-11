package org.example;

import org.junit.Test;

public class ReplaceBlank {

    /**
     * 请实现一个函数，把字符串中的每个空格替换成"%20"，例如“We are happy.“，则输出”We%20are%20happy.“。
     * 1. 第一个指针指向字符串的末尾，第二个指针指向替换之后的字符串的末尾
     * 2. 依次复制字符串的内容，直至第一个指针碰到第一个空格
     * 3. 把第一个空格替换成'%20',把第一个指针抢钱移动一格，把第二个指针向前移动三格
     * 4. 依次向前复制字符串中的字符，直至碰到空格
     * 5. 替换字符串中的倒数第二个空格，把第一个指针向前移动1格，把第二个指针向前移动3格
     * @param s 要转换的字符数组
     * @param usedLen 数组中已经使用的长度
     * @return 转换后数组的长度，-1表示失败
     */
    public int replaceBlank(char[] s, int usedLen) {
        if (s == null || s.length < usedLen) {
            return -1;
        }
        // 统计字符中的空白字符
        int whiteCount = 0;
        for (int i = 0; i < usedLen; i++) {
            if (s[i] == ' ') {
                whiteCount++;
            }
        }
        // 计算转换后的字符串长度是多少
        int targetLen = usedLen + (whiteCount << 1);
        int tmp = targetLen;
        if (targetLen > s.length) {
            return -1;
        }
        // 如果没有空白字符就不用处理
        if (whiteCount == 0) {
            return usedLen;
        }
        // 从后向前，第一个开始处理的字符
        usedLen--;
        // 从后向前, 处理后的字符放置的位置
        targetLen--;

        while (usedLen >= 0 && usedLen < targetLen) {
            if (s[usedLen] == ' ') {
                s[targetLen--] = '0';
                s[targetLen--] = '2';
                s[targetLen--] = '%';
            } else {
                s[targetLen--] = s[usedLen];
            }
            usedLen--;
        }
        return tmp;
    }

    @Test
    public void test() {
        char[] s = new char[50];
        s[0] = ' ';
        s[1] = 'e';
        s[2] = ' ';
        s[3] = ' ';
        s[4] = 'r';
        s[5] = 'e';
        s[6] = ' ';
        s[7] = ' ';
        s[8] = 'a';
        s[9] = ' ';
        s[10] = 'p';
        s[11] = ' ';
        int len = replaceBlank(s, 12);
        System.out.println(new String(s, 0, len));
    }
}
