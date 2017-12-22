package com.mes.io;

import java.io.*;

/**
 * 一句话描述这个类的作用
 * https://www.cnblogs.com/oubo/archive/2012/01/06/2394638.html
 * @author liukl
 * @date 2017/12/19
 */
public class IOUtils {
    public static void main(String [] args) throws Exception{
        fileOut();
    }

    public static void fileOut(){
        byte[] bytes={1,2,3,4,5,6,7,8,9,10};
        OutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream("C:\\c.txt");
            for(int i=0,len=bytes.length;i<len;i++){
                outputStream.write(bytes[i]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        InputStream inputStream=null;
        try {
            inputStream=new FileInputStream("C:\\c.txt");
//            int temp=0;
//            while ((temp=inputStream.read())!=-1){
//                System.out.print(temp);
//            }

            int len = 0;
            byte[] buf = new byte[9];
            while((len=inputStream.read(buf))!=-1){
                System.out.println("xxx"+len);
                for(int i=0;i<len-1;i++){
                    System.out.println(buf[i]);
                }

            }



        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}
