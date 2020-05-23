package cn.yiidii.pigeon.agent.netty.handler;

import cn.yiidii.pigeon.agent.netty.AgentManager;
import cn.yiidii.pigeon.agent.netty.dto.AgentResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@Data
public class AgentHandler extends SimpleChannelInboundHandler<AgentResponse> {

    private static ChannelHandlerContext clientCTX;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AgentResponse response) throws Exception {
        AgentManager.dispatchTask(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clientCTX = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client close ");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Client close  exceptionCaught e: ");
        super.exceptionCaught(ctx, cause);
    }

    public static ChannelHandlerContext getAgentClientCTX() {
        return clientCTX;
    }

    public static String getHost() {
        InetSocketAddress socketAddress = (InetSocketAddress) clientCTX.channel().remoteAddress();
        return socketAddress.getAddress().getHostAddress();
    }
}
