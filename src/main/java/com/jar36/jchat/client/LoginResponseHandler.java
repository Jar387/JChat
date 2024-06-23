package com.jar36.jchat.client;

import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // connect to server
        System.out.println("Logging to server");
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUsername(ClientMain.username);
        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.getSessionToken() == 0) { // login fail
            System.out.println("Cannot login " + loginResponsePacket.getReason());
            System.exit(-1);
        } else {
            System.out.println("Login successfully! Your session token is " + loginResponsePacket.getSessionToken());
            ClientMain.sessionToken = loginResponsePacket.getSessionToken();
        }
        channelHandlerContext.fireChannelActive();
    }
}
