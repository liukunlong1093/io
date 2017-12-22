package com.mes.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 一句话描述这个类的作用
 *
 * @author liukl
 * @date 2017/12/20
 */
public class TimeServerHandler extends ChannelHandlerAdapter{
    private int counter;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body=(String) msg;
        System.out.println("The time server receive order :"+body+";the counter is :"+ ++counter);
        String currTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
        currTime=currTime+System.getProperty("line.separator");
        ByteBuf resp= Unpooled.copiedBuffer(currTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
