package com.jar36.jchat.client;

import com.jar36.jchat.packet.PacketDecoder;
import com.jar36.jchat.packet.PacketEncoder;
import com.sun.istack.internal.Nullable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientMain {
    public static final String about = "JChat client v0.1";
    public static final Path clientCachePath = Paths.get(System.getProperty("user.home") + "/.cache/jchat.cookie");
    @Nullable
    public static String JWTCode;
    public static String username;
    public static String passwdHash;

    public static Channel channel;

    public static void saveJWTCode() throws IOException {
        Files.write(clientCachePath, (JWTCode + '\n' + username).getBytes());
    }

    public static void createJWTCache() throws IOException {
        if (!Files.exists(clientCachePath)) {
            Files.createFile(clientCachePath);
        }
    }

    public static void eraseJWTCache() throws IOException {
        if (Files.exists(clientCachePath)) {
            Files.delete(clientCachePath);
        }
    }

    public static void main(String[] Args) throws IOException {
        // read client JWT cache
        if (Files.exists(clientCachePath)) {
            JWTCode = Files.readAllLines(clientCachePath).get(0);
            username = Files.readAllLines(clientCachePath).get(1);
        }
        // init network
        // uncomment this when publish with ssl
        // SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        // socketChannel.pipeline().addFirst(sslContext.newHandler(socketChannel.alloc()));
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 8, 4));
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginResponseHandler());
                        socketChannel.pipeline().addLast(new ClientGetHandler());
                        socketChannel.pipeline().addLast(new PacketEncoder());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Connected to the server successfully");
            } else {
                UI.error("Cannot connect to the server");
                System.exit(-1);
            }
        });
        channel = channelFuture.channel();
        UI.startLoginInterface();
    }
}
