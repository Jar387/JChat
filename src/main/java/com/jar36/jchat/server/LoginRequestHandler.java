package com.jar36.jchat.server;

import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import com.jar36.jchat.packet.Packet;
import com.jar36.jchat.packet.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

import static com.jar36.jchat.server.ServerMain.logger;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) {
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            logger.info("Client connected, username "+loginRequestPacket.getUsername());
            Random random = new Random();
            loginResponsePacket.setSessionToken(random.nextLong());
            loginResponsePacket.setReason("success");
            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
    }
}
