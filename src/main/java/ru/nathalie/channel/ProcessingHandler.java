package ru.nathalie.channel;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.nathalie.model.dto.RequestDto;
import ru.nathalie.router.Router;

@ChannelHandler.Sharable
public class ProcessingHandler extends ChannelInboundHandlerAdapter {
    private final Router router;

    public ProcessingHandler(Router router) {
        this.router = router;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RequestDto requestDto = new RequestDto((String) msg);
        String responseData = router.parseHeaders(requestDto);

        ChannelFuture future = ctx.writeAndFlush(responseData);
        future.addListener(ChannelFutureListener.CLOSE);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
