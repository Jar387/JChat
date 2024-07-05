package com.jar36.jchat.client;

import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.GetDataPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientGetHandler extends SimpleChannelInboundHandler<GetDataPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetDataPacket msg) throws Exception {
        System.out.println("read0");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("read");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 3rd called
        // may be called later
        GetDataPacket getDataPacket = new GetDataPacket();
        getDataPacket.setSessionToken(ClientMain.sessionToken);
        getDataPacket.setSubfunction(Command.DATA_GET_SESSION_LIST);
        ctx.channel().writeAndFlush(getDataPacket);

        getDataPacket.setSubfunction(Command.DATA_GET_SESSION_MESSAGE);
        getDataPacket.setId(1);
        ctx.channel().writeAndFlush(getDataPacket);
        System.out.println("data sent");
    }
}
