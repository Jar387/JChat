package com.jar36.jchat.client;

import com.jar36.jchat.client.widgets.Label;
import com.jar36.jchat.packet.PacketDecoder;
import com.jar36.jchat.packet.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;

public class ClientMain {
    public static final String about = "JChat client v0.1";
    public static long sessionToken;
    public static String username;

    public static void main(String[] Args) throws IOException, InterruptedException {
        UI.InitializeUI();
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
        bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                Label label = new Label(new String[]{"Connected to the server successfully"}, about);
                label.showLabel(UI.terminal);
                label.removeLabel(UI.terminal);
            } else {
                UI.error("Cannot connect to the server");
            }
        });

    }
}
