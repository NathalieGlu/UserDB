package ru.nathalie.web;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.channel.ClientHandler;

import java.util.Iterator;

public class NettyClient implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class.getName());
    private final String message;
    private ClientHandler handler;

    public NettyClient(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            String host = "127.0.0.1";
            int port = 8080;
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                handler = new ClientHandler();
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast("decoder", new StringDecoder());
                        ch.pipeline().addLast("encoder", new StringEncoder());
                        ch.pipeline().addLast(handler);
                    }
                });

                ChannelFuture f = b.connect(host, port).sync();
                f.channel().writeAndFlush(message);
                f.channel().closeFuture().sync();
            } finally {
                workerGroup.shutdownGracefully().sync();
            }
        } catch (Exception e) {
            log.error("Exception during client execution:", e);
        }
        if (check()) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
        }
    }

    public boolean check() {
        JSONObject response = new JSONObject(handler.getResponse());
        Iterator<String> keys = response.keys();
        String key = keys.next();
        Double from = (Double) ((JSONObject) response.get(key)).get("before") -
                (Double) ((JSONObject) response.get(key)).get("after");

        key = keys.next();
        Double to = (Double) ((JSONObject) response.get(key)).get("after") -
                (Double) ((JSONObject) response.get(key)).get("before");
        return from.equals(to);
    }
}
