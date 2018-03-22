package ru.nathalie.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class.getName());
    private String response;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String response = (String) msg;
        try {
            this.response = response.substring(response.indexOf("{"));
        } catch (Exception e) {
            log.error("Error in response: ", e);
        }
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public String getResponse() {
        return response;
    }
}
