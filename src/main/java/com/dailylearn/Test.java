package com.dailylearn;

import cn.hutool.core.io.FileUtil;
import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedInputStream inputStream = FileUtil.getInputStream("D:\\Desktop\\test.txt");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String str = result.toString("UTF-8");
        System.out.println(str);
        FileUtil.appendUtf8String("\n 你好啊````", "D:\\Desktop\\test.txt");
    }
} 