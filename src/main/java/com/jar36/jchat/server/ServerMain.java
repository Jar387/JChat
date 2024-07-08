package com.jar36.jchat.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.jar36.jchat.LoggerFormatter;
import com.jar36.jchat.packet.PacketDecoder;
import com.jar36.jchat.packet.PacketEncoder;
import com.jar36.jchat.server.data.SqlManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.logging.Logger;

public class ServerMain {
    public static final Logger logger = LoggerFormatter.installFormatter(Logger.getLogger(ServerMain.class.getSimpleName()));
    public static final short port = 8888;
    public static final String issuer = "jchat_server.com";
    public static Algorithm algorithm;
    public static JWTVerifier jwtVerifier;

    public static void main(String[] Args) throws InterruptedException {
        // selfSignedCertificate = new SelfSignedCertificate();
        // SslContext sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();
        algorithm = Algorithm.HMAC512("secret"); // TODO generate new key for every server start
        jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
        logger.info("Generating keypair...done");
        // load user data
        SqlManager.loadDatabase();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

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
                        socketChannel.pipeline().addLast(new ServerGetHandler());
                        socketChannel.pipeline().addLast(new PacketEncoder());
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        logger.info("Server Started at port " + port);
        channelFuture.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
