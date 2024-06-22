package com.jar36.jchat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
    public static void main(String[] Args){
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("Connected to server\n");
            }else{
                System.out.println("Cannot connect to server\n");
                System.exit(-1);
            }
        });

    }
}
