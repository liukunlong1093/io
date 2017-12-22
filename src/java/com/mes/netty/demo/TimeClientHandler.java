package com.mes.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * 一句话描述这个类的作用
 *
 * @author liukl
 * @date 2017/12/20
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private static final Logger logger=Logger.getLogger(TimeClientHandler.class.getName());
    private int counter;

    private byte [] req;

    public TimeClientHandler() {
        req=("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message;
        int count=100;
        for(int i=0;i<count;i++){
            message=Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body=(String) msg;
        System.out.println("Now is : "+body+"; the counter is:"+ ++counter);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       logger.warning("Unexpected exception from downstream :"+cause.getMessage());
       ctx.close();
    }
}
