package com.mes.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * 时间服务处理类
 * @author liukl
 * @date 2017/12/19
 */
public class TimeServerHandler implements Runnable{
    private Socket socket;
    public TimeServerHandler(Socket socket){
        this.socket=socket;
    }
    public void run() {
        BufferedReader in=null;
        PrintWriter out=null;
        try {
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(this.socket.getOutputStream(),true);
            String currTime=null;
            String body=null;
            while (true){
                body=in.readLine();
                if(body==null){
                    break;
                }
                System.out.println("this time server receive order:"+body);
                currTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";

                out.println(currTime);
            }
        }catch (Exception e){
            if(in!=null){
                try {
                    in.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
            if(out!=null){
                out.close();
                out=null;
            }
            if(this.socket!=null){
                try {
                    this.socket.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
                this.socket=null;
            }
        }
    }
}
