package com.jar36.jchat.server;

import com.jar36.jchat.packet.MessagePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.jar36.jchat.server.ServerMain.logger;

public class ServerMessageHandler extends SimpleChannelInboundHandler<MessagePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessagePacket messagePacket) {
        // broadcast raw packet
        for(User u:User.users){
            u.getChannel().writeAndFlush(messagePacket);
        }
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof MessagePacket){
            super.channelRead(ctx, msg);
        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
