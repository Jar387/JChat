package com.jar36.jchat.client;

import com.jar36.jchat.packet.PacketDecoder;
import com.jar36.jchat.packet.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
    public static long sessionToken;

    public static void main(String[] Args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginResponseHandler());
                        socketChannel.pipeline().addLast(new PacketEncoder());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Connected to server");
            } else {
                System.out.println("Cannot connect to server");
                System.exit(-1);
            }
        });

    }
}
