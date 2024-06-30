package com.jar36.jchat.server;

import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import com.jar36.jchat.server.data.UserData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.Random;

import static com.jar36.jchat.server.ServerMain.logger;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    private void loginHandler(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSubfunction(loginRequestPacket.getSubfunction());
        for (UserData ud : UserData.userData) { // on disk database
            if (ud.getName().compareTo(loginRequestPacket.getUsername()) == 0) { // user exist
                for (User u : User.users) {
                    if (u.getUserData().getName().compareTo(loginRequestPacket.getUsername()) == 0) { // already logged in
                        loginResponsePacket.setSessionToken(0);
                        loginResponsePacket.setReason("Cannot login: your account already logged in");
                        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
                        return;
                    }
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
                    loginResponsePacket.setReason("success");
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
        }
        loginResponsePacket.setSessionToken(0);
        loginResponsePacket.setReason("Cannot login: user not exist");
        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
    }

    private void createUserHandler(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws IOException {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSessionToken(0);
        loginResponsePacket.setSubfunction(loginRequestPacket.getSubfunction());
        if (loginRequestPacket.getUsername().isEmpty() || loginRequestPacket.getUsername().length() > 255) { // check username length
            loginResponsePacket.setReason("Cannot create user: illegal username length");
            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
        }
        for (UserData ud : UserData.userData) { // check user exists
            if (ud.getName().compareTo(loginRequestPacket.getUsername()) == 0) {
                loginResponsePacket.setReason("Cannot create user: username exists");
                channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
            }
        }
        UserData userData = new UserData();
        userData.setUid(UserData.userData.size() + 1);
        userData.setName(loginRequestPacket.getUsername());
        userData.setPasswdHash(loginRequestPacket.getPasswdHash());
        UserData.userData.add(userData);
        loginResponsePacket.setReason("success");
        loginResponsePacket.setSessionToken(1);
        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
    }

//    private void changePasswdHandler(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws IOException {
//        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
//        loginResponsePacket.setSessionToken(0);
//        for (UserData ud : UserData.userData) { // check user exists
//            if (ud.getName().compareTo(loginRequestPacket.getUsername()) == 0) {
//                if (ud.getPasswdHash().compareTo(loginRequestPacket.getOldPasswdHash()) == 0) {
//                    ud.setPasswdHash(loginRequestPacket.getPasswdHash());
//                    loginResponsePacket.setSessionToken(1);
//                    loginResponsePacket.setReason("success");
//                    channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
//                    UserDataManager.saveUserData();
//                } else {
//                    loginResponsePacket.setReason("Cannot change password: old password error");
//                    channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
//                }
//                return;
//            }
//        }
//        loginResponsePacket.setReason("Cannot change password: user not exist");
//        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
//    }
//
//    private void changeUserNameHandler(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws IOException {
//        String ip = channelHandlerContext.channel().remoteAddress().toString();
//        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
//        loginResponsePacket.setSessionToken(0);
//        for (User u : User.users) {
//            if (ip.compareTo(u.getIp()) == 0) { // find user logged in
//                for (UserData ud : UserData.userData) {
//                    if (ud == u.getUserData()) {
//                        if (loginRequestPacket.getUsername().isEmpty() || loginRequestPacket.getUsername().length() > 255) { // check username length
//                            loginResponsePacket.setReason("Cannot change username: illegal username length");
//                            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
//                        } else {
//                            loginResponsePacket.setSessionToken(1);
//                            ud.setName(loginRequestPacket.getUsername());
//                            loginResponsePacket.setReason("success");
//                            channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
//                            UserDataManager.saveUserData();
//                        }
//                        return;
//                    }
//                }
//                loginResponsePacket.setReason("Cannot change username: server internal error");
//                channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
//            }
//        }
//        loginResponsePacket.setReason("Cannot change username: client internal error");
//        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws IOException {
        switch (loginRequestPacket.getSubfunction()) {
            case Command.LOGIN_REQUEST_SUBFUNCTION_LOGIN:
                loginHandler(channelHandlerContext, loginRequestPacket);
                break;
            case Command.LOGIN_REQUEST_SUBFUNCTION_CREATE_USER:
                createUserHandler(channelHandlerContext, loginRequestPacket);
                break;
//            case Command.LOGIN_REQUEST_SUBFUNCTION_CHANGE_PASSWD:
//                changePasswdHandler(channelHandlerContext, loginRequestPacket);
//                break;
//            case Command.LOGIN_REQUEST_SUBFUNCTION_CHANGE_USERNAME:
//                changeUserNameHandler(channelHandlerContext, loginRequestPacket);
//                break;
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
        logger.warning("Cannot find disconnected user in registered user list");
    }
}
