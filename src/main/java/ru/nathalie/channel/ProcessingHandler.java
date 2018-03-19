package ru.nathalie.channel;

import io.netty.channel.*;
import ru.nathalie.model.data.RequestData;
import ru.nathalie.router.Router;

@ChannelHandler.Sharable
public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    private final Router router;

    public ProcessingHandler(Router router) {
        this.router = router;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RequestData requestData = new RequestData((String) msg);
        String responseData = router.parseHeaders(requestData);

        ChannelFuture future = ctx.writeAndFlush(responseData);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
