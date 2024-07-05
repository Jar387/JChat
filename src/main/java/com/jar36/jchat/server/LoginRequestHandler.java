package com.jar36.jchat.server;

import com.jar36.jchat.SqlHelper;
import com.jar36.jchat.Util;
import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import com.jar36.jchat.server.data.SqlManager;
import com.jar36.jchat.server.data.UserData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Random;

import static com.jar36.jchat.server.ServerMain.logger;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    private void loginHandler(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSubfunction(loginRequestPacket.getSubfunction());
        UserData ud = SqlHelper.queryTableToObject(SqlManager.userDataBaseStatement, UserData.class, "name", loginRequestPacket.getUsername());
        if (ud != null) { // user exist
            if (Util.verifyUsername(loginRequestPacket.getUsername()) != null) { // already logged in
                loginResponsePacket.setSessionToken(0);
                loginResponsePacket.setReason("Cannot login: your account already logged in");
                channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
                return;
            }
            if (ud.getPasswdHash().compareTo(loginRequestPacket.getPasswdHash()) == 0) { // passwd ok
                User user = new User();
                user.setIp(channelHandlerContext.channel().remoteAddress().toString());
                user.setChannel(channelHandlerContext.channel());
                user.setUserData(ud);
                logger.info("Client connected, username " + ud.getName() + " ip " + user.getIp());
                Random random = new Random();
                long sessionToken = random.nextLong();
                loginResponsePacket.setSessionToken(sessionToken);
                user.setSessionToken(sessionToken);
                channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
                User.users.add(user); // in memory database
                return;
            }
            loginResponsePacket.setSessionToken(0);
            loginResponsePacket.setReason("Cannot login: password error");
            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
            return;
        }
        loginResponsePacket.setSessionToken(0);
        loginResponsePacket.setReason("Cannot login: user not exist");
        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
    }

    private void createUserHandler(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws IOException, SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSessionToken(0);
        loginResponsePacket.setSubfunction(loginRequestPacket.getSubfunction());
        if (loginRequestPacket.getUsername().isEmpty() || loginRequestPacket.getUsername().length() > 255) { // check username length
            loginResponsePacket.setReason("Cannot create user: illegal username length");
            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
            return;
        }
        UserData ud = SqlHelper.queryTableToObject(SqlManager.userDataBaseStatement, UserData.class, "name", loginRequestPacket.getUsername()); // check user exists
        if (ud != null) {
            loginResponsePacket.setReason("Cannot create user: username exists");
            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
            return;
        }
        UserData userData = new UserData();
        userData.setUid(SqlManager.nextUid);
        SqlManager.nextUid++;
        userData.setName(loginRequestPacket.getUsername());
        userData.setPasswdHash(loginRequestPacket.getPasswdHash());
        SqlHelper.insertObject(SqlManager.userDataBaseStatement, UserData.class, userData);
        loginResponsePacket.setSessionToken(1);
        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws IOException, SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        switch (loginRequestPacket.getSubfunction()) {
            case Command.LOGIN_REQUEST_SUBFUNCTION_LOGIN:
                loginHandler(channelHandlerContext, loginRequestPacket);
                break;
            case Command.LOGIN_REQUEST_SUBFUNCTION_CREATE_USER:
                createUserHandler(channelHandlerContext, loginRequestPacket);
                break;
            default:
                LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
                loginResponsePacket.setSessionToken(0);
                channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof LoginRequestPacket) {
            super.channelRead(ctx, msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (ctx == null || ctx.channel() == null || ctx.channel().remoteAddress() == null) {
            return;
        }
        String ip = ctx.channel().remoteAddress().toString();
        for (User u : User.users) {
            if (ip.compareTo(u.getIp()) == 0) {
                User.users.remove(u);
                logger.info("Client disconnected, username " + u.getUserData().getName());
                return;
            }
        }
    }
}
