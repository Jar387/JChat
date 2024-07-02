package com.jar36.jchat.server;

import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.GetDataPacket;
import com.jar36.jchat.packet.Message;
import com.jar36.jchat.server.data.SessionMessageManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

public class ServerGetHandler extends SimpleChannelInboundHandler<GetDataPacket> {
    private void getSessionMessage(ChannelHandlerContext ctx, GetDataPacket msg) throws IOException {
        msg.setSessionToken(0);
        msg.setMessages(SessionMessageManager.readSessionDatabase(msg.getId()).toArray(new Message[0]));
        ctx.channel().writeAndFlush(msg);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof GetDataPacket) {
            super.channelRead(ctx, msg);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetDataPacket msg) throws Exception {
        switch (msg.getSubfunction()){
            case Command.DATA_GET_SESSION_LIST:
                getSessionMessage(ctx, msg);
                break;
            case Command.DATA_GET_SESSION_MESSAGE: break;
        }
    }
}
