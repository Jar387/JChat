package com.jar36.jchat.server;

import com.jar36.jchat.packet.PacketDecoder;
import com.jar36.jchat.packet.PacketEncoder;
import com.jar36.jchat.server.data.UserDataManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.logging.Logger;

public class ServerMain {
    public static final Logger logger = Logger.getLogger("server");

    public static void main(String[] Args) throws InterruptedException, IOException {
        // selfSignedCertificate = new SelfSignedCertificate();
        // SslContext sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();

        // load user data
        UserDataManager.loadDatabase();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // debug user data
//        UserData userData = new UserData();
//        userData.setUid(1);
//        userData.setName("admin");
//        userData.setPasswdHash(Util.sha256("admin"));
//        UserData.userData.add(userData);
//        UserDataManager.saveUserData();


        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        // socketChannel.pipeline().addFirst(sslContext.newHandler(socketChannel.alloc()));
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 8, 4));
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginRequestHandler());
                        socketChannel.pipeline().addLast(new ServerMessageHandler());
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
