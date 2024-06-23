package com.jar36.jchat.server;

import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

import static com.jar36.jchat.server.ServerMain.logger;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) {
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            for(User u:User.users){
                if(u.getName().compareTo(loginRequestPacket.getUsername())==0){
                    loginResponsePacket.setSessionToken(0);
                    loginResponsePacket.setReason("your username already exists");
                    channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
                    return;
                }
            }
            User user = new User();
            user.setName(loginRequestPacket.getUsername());
            user.setIp(channelHandlerContext.channel().remoteAddress().toString());
            user.setChannel(channelHandlerContext.channel());
            logger.info("Client connected, username "+user.getName()+" ip "+user.getIp());
            Random random = new Random();
            long sessionToken = random.nextLong();
            loginResponsePacket.setSessionToken(sessionToken);
            loginResponsePacket.setReason("success");
            user.setSessionToken(sessionToken);
            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
            User.users.add(user);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof LoginRequestPacket){
            super.channelRead(ctx, msg);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if(ctx==null||ctx.channel()==null||ctx.channel().remoteAddress()==null){
            return;
        }
        String ip = ctx.channel().remoteAddress().toString();
        for(User u:User.users){
            if(ip.compareTo(u.getIp())==0){
                User.users.remove(u);
                logger.info("Client disconnected, username "+u.getName());
                return;
            }
        }
        logger.warning("Cannot find disconnected user in registered user list");
    }
}
