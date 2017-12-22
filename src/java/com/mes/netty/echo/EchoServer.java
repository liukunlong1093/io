package com.mes.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 一句话描述这个类的作用
 *
 * @author liukl
 * @date 2017/12/21
 */
public class EchoServer {
    public void bind(int port) throws Exception{
        //配置服务端的NIO线程组 boss work
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ByteBuf delimiter= Unpooled.copiedBuffer("$_".getBytes());
                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                    socketChannel.pipeline().addLast(new StringDecoder());
                }
            });
            //绑定端口同步等待成功
            ChannelFuture f=bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出释放线程池资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port=8080;
        if(args!=null && args.length>0){
            try {
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new EchoServer().bind(port);
    }
}
