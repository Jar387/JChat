package com.jar36.jchat.client;

import com.jar36.jchat.packet.GetDataPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientGetHandler extends SimpleChannelInboundHandler<GetDataPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetDataPacket msg) {
        System.out.println("read0");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("read");
        super.channelRead(ctx, msg);
    }
}
