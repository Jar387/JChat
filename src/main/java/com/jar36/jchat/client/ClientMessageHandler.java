package com.jar36.jchat.client;

import com.jar36.jchat.packet.MessagePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Scanner;

public class ClientMessageHandler extends SimpleChannelInboundHandler<MessagePacket> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // start a new thread to get user input
        new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            MessagePacket messagePacket = new MessagePacket();
            messagePacket.setUsername(ClientMain.username);
            while(true){
                System.out.print(">>>");
                String input = scanner.nextLine();
                if(input.compareTo("q")==0){
                    ctx.close();
                    System.exit(0);
                }
                messagePacket.setMsg(input);
                ctx.executor().submit(()->{
                    ctx.channel().writeAndFlush(messagePacket);
                });
            }
        }).start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessagePacket messagePacket) {
        String user = messagePacket.getUsername();
        String msg = messagePacket.getMsg();
        System.out.println(user+": "+msg);
    }
}
