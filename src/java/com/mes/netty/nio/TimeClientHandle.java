package com.mes.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 一句话描述这个类的作用
 *
 * @author liukl
 * @date 2017/12/20
 */
public class TimeClientHandle implements Runnable{
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;
    public TimeClientHandle(String host,int port){
        this.host=(host==null ? "127.0.0.1":host);
        this.port=port;
        try {
            selector=Selector.open();
            socketChannel=SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while(!stop){
            try {
                selector.select(1000);
                Set <SelectionKey> selectedKeys=selector.selectedKeys();
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
            } catch (IOException e) {

                //System.exit(1);
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
            SocketChannel sc= (SocketChannel) key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    doWrite(sc);
                }else{
                    System.exit(1);
                }
            }
            if(key.isReadable()){
                ByteBuffer readBuffer=ByteBuffer.allocate(1024);
                int readBytes=sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body=new String(bytes,"UTF-8");
                    System.out.println("Now is :"+body);
                    this.stop=true;
                }else if(readBytes<0){
                    key.cancel();
                    sc.close();
                }else{
                    //读到0字节忽略
                }

            }
        }
    }
    private void doConnect()throws  IOException{
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel sc) throws  IOException{
        byte[] req="QUERY TIME ORDER".getBytes();
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);
        if(!byteBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }
    }
}
