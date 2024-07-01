package com.jar36.jchat.client;

import com.jar36.jchat.packet.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.util.Date;

public class ClientMain {
    public static long sessionToken;
    public static String username;

    public static void main(String[] Args) {
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
        bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Connected to server");
            } else {
                System.out.println("Cannot connect to server");
                System.exit(-1);
            }
        });

    }
    private static void printAsString(ByteBuf buffer) {
        System.out.println("As String:");
        if (buffer.isReadable()) {
            // Convert the readable bytes to a string using UTF-8 encoding
            String str = buffer.toString(CharsetUtil.UTF_8);
            System.out.println(str);
        } else {
            System.out.println("Buffer is not readable.");
        }
    }
}
