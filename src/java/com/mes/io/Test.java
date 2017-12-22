package com.mes.io;

import java.io.*;

/**
 * 一句话描述这个类的作用
 *
 * @author liukl
 * @date 2017/12/19
 */
public class Test {
    /**
     * 编写一个程序，将a.txt文件中的单词与b.txt文件中的单词交替合并到c.txt文件中，a.txt文件中的单词用回车符分隔，b.txt文件中用回车或空格进行分隔。
     * @param args
     */
    public static void main(String [] args) throws Exception{
        Reader reader=new InputStreamReader(new FileInputStream(new File("C:\\b.txt")));
        BufferedReader bufferedReader=new BufferedReader(reader);
        String msg=bufferedReader.readLine();
        String [] m=msg.split("\\b \\b");
        System.out.println(m.length);
        System.out.println(bufferedReader.readLine());
        reader.close();
    }
}
