package com.jar36.jchat.client;

import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof UserEvent) {
            UserEvent userEvent = (UserEvent) evt;
            if (userEvent.getEventID() == UserEvent.LOGIN_TRIGGERED) {
                LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
                loginRequestPacket.setUsername(ClientMain.username);
                loginRequestPacket.setPasswdHash(ClientMain.passwdHash);
                loginRequestPacket.setSubfunction(Command.LOGIN_REQUEST_SUBFUNCTION_LOGIN);
                ctx.channel().writeAndFlush(loginRequestPacket);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof LoginResponsePacket) {
            super.channelRead(ctx, msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) {
        // 2nd called
        // may be called later?
        if (loginResponsePacket.getSessionToken() == 0) { // login fail
            System.out.println(loginResponsePacket.getReason());
            System.exit(-1);
        } else {
            System.out.println("Login successfully! Your session token is " + loginResponsePacket.getSessionToken());
            ClientMain.sessionToken = loginResponsePacket.getSessionToken();
        }
        channelHandlerContext.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Connection to server was lost! exiting");
        System.exit(0);
    }
}
