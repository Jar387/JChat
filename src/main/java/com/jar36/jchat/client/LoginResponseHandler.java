package com.jar36.jchat.client;

import com.jar36.jchat.Util;
import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.LoginRequestPacket;
import com.jar36.jchat.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Scanner;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // connect to server
        System.out.println("Logging to server");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Input username: ");
        ClientMain.username = scanner.nextLine();

//        Terminal terminal = TerminalBuilder.builder().system(true).build();
//        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
//
//        String passwdHash = Util.sha256(lineReader.readLine("Input password: ", '*'));

        System.out.print("Input password: ");
        String passwdHash = Util.sha256(scanner.nextLine());

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUsername(ClientMain.username);
        loginRequestPacket.setPasswdHash(passwdHash);
        loginRequestPacket.setSubfunction(Command.LOGIN_REQUEST_SUBFUNCTION_LOGIN);
        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.getSessionToken() == 0) { // login fail
            System.out.println(loginResponsePacket.getReason());
            System.exit(-1);
        } else {
            System.out.println("Login successfully! Your session token is " + loginResponsePacket.getSessionToken());
            ClientMain.sessionToken = loginResponsePacket.getSessionToken();
        }
        channelHandlerContext.fireChannelActive();
    }
}
