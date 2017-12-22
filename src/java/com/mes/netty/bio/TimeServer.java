package com.mes.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 时间服务器
 *
 * @author liukl
 * @date 2017/12/19
 */
public class TimeServer {
    /**
     * 主要方法
     * @param args 参数
     */
    public static void main(String [] args) throws IOException{
        int port =8080;
        if(args!=null && args.length>0){
            try {
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        ServerSocket server=null;
        try {
            server=new ServerSocket(port);
            System.out.println("this is TimeServer start in port:"+port);
            Socket socket=null;
            //伪异步IO
            TimeServerHandlerExecutePool serverHandlerExecutePool=new TimeServerHandlerExecutePool(50,10000);

            while (true){
                socket=server.accept();
                //一般处理
                //new Thread(new TimeServerHandler(socket)).start();
                //伪异步处理
                serverHandlerExecutePool.execute(new TimeServerHandler(socket));
            }
        }finally {
            if(server!=null){
                System.out.println("the time server close");
            }
            server.close();
            server=null;
        }
    }
}
