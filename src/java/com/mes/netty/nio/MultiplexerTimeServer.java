package com.mes.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 一句话描述这个类的作用
 *
 * @author liukl
 * @date 2017/12/20
 */
public class MultiplexerTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel servChannel;
    private volatile boolean stop;

    public MultiplexerTimeServer(int port){
        try {
            this.selector=Selector.open();
            this.servChannel=ServerSocketChannel.open();
            this.servChannel.configureBlocking(false);
            this.servChannel.socket().bind(new InetSocketAddress(port),1024);
            this.servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("the time server is start in port:"+port);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void stop(){
        this.stop=true;
    }
    public void run() {
        while(!stop){
            try {
                System.out.println(selector.toString());
                selector.select(1000);
                Set<SelectionKey> selectedKeys=selector.selectedKeys();
                Iterator<SelectionKey> it=selectedKeys.iterator();
                SelectionKey key=null;
                while(it.hasNext()){
                    key=it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                key.channel().close();
                            }
                        }
                    }

                }
            }catch (Throwable t){
                t.printStackTrace();
            }
//            if(selector!=null){
//                try {
//                    selector.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    private void handleInput(SelectionKey key) throws  IOException{
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
                SocketChannel sc=ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                SocketChannel sc=(SocketChannel)key.channel();
                ByteBuffer readBuffer= ByteBuffer.allocate(1024);
                int readBytes=sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body=new String(bytes,"UTF-8");
                    System.out.println("this time server receive order:"+body);
                    String currTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
                    doWrite(sc,currTime);
                }
            }

        }
    }

    private void doWrite(SocketChannel channel,String response) throws IOException {
        if(response!=null && response.trim().length()>0){
            byte[] bytes=response.getBytes();
            ByteBuffer writeBuffter=ByteBuffer.allocate(bytes.length);
            writeBuffter.put(bytes);
            writeBuffter.flip();
            channel.write(writeBuffter);
        }
    }
}
