package com.mes.netty.nio;

/**
 * 一句话描述这个类的作用
 *
 * @author liukl
 * @date 2017/12/20
 */
public class TimeServer {
    public static void main(String[] args){
        int port =8080;
        if(args!=null && args.length>0){
            try {
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        MultiplexerTimeServer timeServer=new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();

    }
}
