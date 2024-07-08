package com.jar36.jchat.client;

import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.swing.*;
import java.io.IOException;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof UserEvent) {
            UserEvent userEvent = (UserEvent) evt;
            if (userEvent.getEventID() == UserEvent.LOGIN_TRIGGERED) {
                LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
                loginRequestPacket.setSubfunction(Command.LOGIN_REQUEST_SUBFUNCTION_LOGIN);
                if (ClientMain.JWTCode != null && UI.loginInterface.isAutoLogin()) {
                    loginRequestPacket.setJWTCode(ClientMain.JWTCode);
                } else {
                    loginRequestPacket.setUsername(ClientMain.username);
                    loginRequestPacket.setPasswdHash(ClientMain.passwdHash);
                    ClientMain.JWTCode = null;
                    ClientMain.eraseJWTCache();
                }
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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) throws IOException {
        if (loginResponsePacket.getSubfunction() == 0) { // login fail
            JOptionPane.showConfirmDialog(null, loginResponsePacket.getReason(), "Login failed", JOptionPane.DEFAULT_OPTION);
            return;
        } else {
            if (UI.loginInterface.isAutoLogin()) {
                ClientMain.JWTCode = loginResponsePacket.getJWTCode();
                ClientMain.createJWTCache();
                ClientMain.saveJWTCode();
            }
        }
        UI.startMainInterface();
        channelHandlerContext.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        UI.error("Connection to server was lost!");
    }
}
