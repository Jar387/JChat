package com.jar36.jchat.server;

import com.jar36.jchat.packet.PacketDecoder;
import com.jar36.jchat.packet.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Logger;

public class ServerMain {
    public static final Logger logger = Logger.getLogger("server");
    public static void main(String[] Args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginRequestHandler());
                        socketChannel.pipeline().addLast(new PacketEncoder());
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        logger.info("Server Started");
        ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
        channelFuture.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
